package org.hardsign.services.updateHandlers.keyboardPressHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.Emoji;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.requests.DeleteActivityRequest;
import org.hardsign.models.activities.requests.GetActivityByIdRequest;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.UserState;
import org.hardsign.services.users.UserStateService;

import java.util.Objects;

public class AcceptDeleteActivityPressHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;

    public AcceptDeleteActivityPressHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            UserStateService userStateService) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
    }

    @Override
    public void handle(Update update, UpdateContext context) throws Exception {
        if (!context.isRegistered())
            return;

        var user = update.message().from();
        if (user.isBot())
            return;

        if (context.getState() != UserState.DeleteActivityConfirmation)
            return;

        var message = update.message().text();
        if (!Objects.equals(message, ButtonNames.ACCEPT_DELETE.getName()))
            return;

        var chatId = update.message().chat().id();
        var state = userStateService.getState(user);
        var activityId = state.getDeleteActivityId();

        userStateService.setState(user, UserState.None); // todo: (tebaikin) 06.11.2022 refactor with patch
        userStateService.setDeleteActivity(user, 0);
        context.setState(UserState.None);

        if (activityId == 0) {
            var noActivityMessage = "Произошла ошибка! Не нашли выбранную активность. Попробуйте заново (по-братски)";
            bot.execute(new SendMessage(chatId, noActivityMessage)
                                .replyMarkup(KeyboardFactory.createMainMenu(context, jikanApiClient)));
            return;
        }

        var activityRequest = new BotRequest<>(new GetActivityByIdRequest(activityId), context.getMeta());
        var activity = jikanApiClient.activities().getById(activityRequest).getValueOrThrow();

        var request = new BotRequest<>(new DeleteActivityRequest(activity.getId()), context.getMeta());
        jikanApiClient.activities().delete(request).ensureSuccess();

        bot.execute(new SendMessage(chatId, "Вы удалили активность " + activity.getName() + " " + Emoji.Pensive.value())
                            .replyMarkup(KeyboardFactory.createMainMenu(context, jikanApiClient)));
    }
}
