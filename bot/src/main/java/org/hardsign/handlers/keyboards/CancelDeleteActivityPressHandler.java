package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
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

        userStateService.with(context).update(user, UserStatePatch.createDefault());

        var activityId = state.getDeleteActivityId();
        if (activityId == 0) {
            handleNotFoundActivity(bot, chatId, context);
            return;
        }

        var text = "Ура! Активность не была удалена! Можете продолжать трекать время :)";
        sendDefaultMenuMessage(bot, context, chatId, text);
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
