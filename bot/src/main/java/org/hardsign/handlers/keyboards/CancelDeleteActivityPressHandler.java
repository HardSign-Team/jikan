package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.handlers.keyboards.abstracts.ConfirmationDeleteActivityPressHandler;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.services.users.UserStateService;

public class CancelDeleteActivityPressHandler extends ConfirmationDeleteActivityPressHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final UserStateService userStateService;

    public CancelDeleteActivityPressHandler(TelegramBot bot, UserStateService userStateService) {
        this.bot = bot;
        this.userStateService = userStateService;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var state = userStateService.getState(user);
        var activityId = state.getDeleteActivityId();

        userStateService.with(context).update(user, UserStatePatch.createDefault());

        if (activityId == 0) {
            handleNotFoundActivity(bot, chatId, context);
            return;
        }

        var text = "Ура! Активность не была удалена! Можете продолжать трекать время :)";
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    @Override
    protected String expectedText() {
        return ButtonNames.CANCEL_DELETE.getName();
    }

    @Override
    protected State requiredState() {
        return State.DeleteActivityConfirmation;
    }
}
