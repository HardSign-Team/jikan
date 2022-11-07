package org.hardsign.handlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.GetActivityByIdRequest;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.handlers.BaseUpdateHandler;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.ValidationHelper;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class SelectActivityCommandHandler extends BaseUpdateHandler implements CommandHandler {

    public static final String commandPrefix = "/sa_";
    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;
    private final Pattern commandPattern = Pattern.compile(commandPrefix + "(\\d+)");

    public SelectActivityCommandHandler(TelegramBot bot, JikanApiClient jikanApiClient, UserStateService userStateService) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var text = update.message().text();
        if (text == null)
            return;

        var matcher = commandPattern.matcher(text);
        if (!matcher.matches())
            return;

        var chatId = update.message().chat().id();
        var activityId = Long.parseLong(matcher.group(1));
        var activity = getActivity(activityId, context.getMeta());
        if (activity == null) {
            sendMessage(chatId, "Активность не найдена :(", context);
            return;
        }

        if (ValidationHelper.isOwnActivity(context.getUser(), activity)) {
            sendMessage(chatId, "Активность не найдена :(", context);
            return;
        }

        handleSuccess(user, context, chatId, activity);
    }

    private void handleSuccess(User user, UpdateContext context, Long chatId, ActivityDto activity) throws Exception {
        userStateService.setActivity(user, activity.getId());
        context.setActivityId(activity.getId());
        sendMessage(chatId, "Вы выбрали активность: " + activity.getName(), context);
    }

    @Nullable
    private ActivityDto getActivity(long activityId, TelegramUserMeta meta) throws Exception {
        var activityRequest = new BotRequest<>(new GetActivityByIdRequest(activityId), meta);
        var result = jikanApiClient.activities().getById(activityRequest);
        return result.notFound() ? null : result.getValueOrThrow();
    }

    private void sendMessage(Long chatId, String text, UpdateContext context) throws Exception {
        var keyboard = KeyboardFactory.createMainMenu(context, jikanApiClient);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }
}

