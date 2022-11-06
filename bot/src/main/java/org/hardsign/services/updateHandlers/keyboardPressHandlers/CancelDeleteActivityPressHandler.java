package org.hardsign.services.updateHandlers.keyboardPressHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.UserState;
import org.hardsign.services.users.UserStateService;

import java.util.Objects;

public class CancelDeleteActivityPressHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;

    public CancelDeleteActivityPressHandler(
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
        if (!Objects.equals(message, ButtonNames.CANCEL_DELETE.getName()))
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

        bot.execute(new SendMessage(chatId, "Ура! Активность не была удалена! Можете продолжать трекать время :)")
                            .replyMarkup(KeyboardFactory.createMainMenu(context, jikanApiClient)));
    }
}
