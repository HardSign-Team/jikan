package org.hardsign.services.updateHandlers.keyboardPressHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.HttpCodes;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.requests.GetActivityByIdRequest;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.requests.GetUserByIdRequest;
import org.hardsign.services.users.UserStateService;

import java.util.regex.Pattern;

public class SelectActivityPressHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;
    private final Pattern commandPattern = Pattern.compile("/sa_(\\d+)");

    public SelectActivityPressHandler(TelegramBot bot, JikanApiClient jikanApiClient, UserStateService userStateService) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
    }

    @Override
    public void handle(Update update, UpdateContext context) throws Exception {
        var user = update.message().from();
        if (user.isBot())
            return;

        if (!context.isRegistered())
            return;

        if (!context.getState().isDefault())
            return;

        var message = update.message().text();
        var matcher = commandPattern.matcher(message);
        if (!matcher.matches())
            return;

        var chatId = update.message().chat().id();

        var activityId = Long.parseLong(matcher.group(1));
        var activityRequest = new BotRequest<>(new GetActivityByIdRequest(activityId), context.getMeta());
        var activityResponse = jikanApiClient.activities().getById(activityRequest);
        if (HttpCodes.NotFound.is(activityResponse.getCode())) {
            bot.execute(new SendMessage(chatId, "Активность не найдена :(")); // todo: (tebaikin) 05.11.2022 стоит показать меню
            return;
        }
        var activity = activityResponse.getValueOrThrow();

        var userRequest = new BotRequest<>(new GetUserByIdRequest(activity.getUserId()), context.getMeta());
        var userResponse = jikanApiClient.users().getUserById(userRequest);
        if (HttpCodes.NotFound.is(userResponse.getCode())) {
            bot.execute(new SendMessage(chatId, "Активность не найдена :(")); // todo: (tebaikin) 05.11.2022 стоит показать меню
            return;
        }

        userStateService.setActivity(user, activityId);
        context.setActivityId(activityId);

        bot.execute(new SendMessage(chatId, "Вы выбрали активность: " + activity.getName())
                            .replyMarkup(KeyboardFactory.createMainMenu(context, jikanApiClient)));
    }
}

