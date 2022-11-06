package org.hardsign.services.updateHandlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.Emoji;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.requests.GetActivityByIdRequest;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.UserState;
import org.hardsign.services.users.UserStateService;

import java.util.Optional;
import java.util.regex.Pattern;

public class DeleteActivityCommandHandler implements CommandHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;
    private final Pattern commandPattern = Pattern.compile("/dela_(\\d+)");

    public DeleteActivityCommandHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            UserStateService userStateService) {
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

        var text = update.message().text();
        if (text == null)
            return;

        var matcher = commandPattern.matcher(text);
        if (!matcher.matches()) {
            return;
        }

        var activityId = Long.parseLong(matcher.group(1));

        var chatId = update.message().chat().id();
        if (context.getActivityId() == activityId) {
            bot.execute(new SendMessage(chatId, "Ай-яй-яй! Нельзя удалить текущую активность.")
                                .replyMarkup(KeyboardFactory.createMainMenu(context, jikanApiClient)));
            return;
        }

        var request = new BotRequest<>(new GetActivityByIdRequest(activityId), context.getMeta());
        var activity = jikanApiClient.activities().getById(request).getValueOrThrow();

        var isOwnActivity = Optional.ofNullable(context.getUser())
                .map(x -> x.getId() == activity.getUserId())
                .orElse(false);
        if (!isOwnActivity) {
            var notOwnText = "Ой! А это не ваша активность, вы ее удалить не можете " + Emoji.FaceWithTongue.value();
            bot.execute(new SendMessage(chatId, notOwnText));
            return;
        }


        userStateService.setState(user, UserState.DeleteActivityConfirmation);
        userStateService.setDeleteActivity(user, activityId);
        context.setState(UserState.DeleteActivityConfirmation);

        var replyMarkup = new ReplyKeyboardMarkup(
                ButtonNames.ACCEPT_DELETE.getName(),
                ButtonNames.CANCEL_DELETE.getName())
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);
        bot.execute(new SendMessage(chatId, "Вы уверены, что хотите удалить активность " + activity.getName() + "?")
                            .replyMarkup(replyMarkup));
    }
}
