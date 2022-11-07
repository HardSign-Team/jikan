package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.UserState;
import org.hardsign.handlers.keyboards.abstracts.ConfirmationDeleteActivityPressHandler;
import org.hardsign.services.users.UserStateService;

public class CancelDeleteActivityPressHandler extends ConfirmationDeleteActivityPressHandler implements KeyboardPressHandler {

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
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var state = userStateService.getState(user);
        var activityId = state.getDeleteActivityId();

        clearState(user, userStateService, context);

        if (activityId == 0) {
            handleNotFoundActivity(bot, jikanApiClient, chatId, context);
            return;
        }

        var text = "Ура! Активность не была удалена! Можете продолжать трекать время :)";
        var keyboard = KeyboardFactory.createMainMenu(context, jikanApiClient);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    @Override
    protected String expectedText() {
        return ButtonNames.CANCEL_DELETE.getName();
    }

    @Override
    protected UserState requiredState() {
        return UserState.DeleteActivityConfirmation;
    }
}
