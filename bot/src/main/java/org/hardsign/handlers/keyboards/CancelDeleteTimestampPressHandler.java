package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.models.users.StateData;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.services.users.UserStateService;

public class CancelDeleteTimestampPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final UserStateService userStateService;

    public CancelDeleteTimestampPressHandler(TelegramBot bot, UserStateService userStateService) {
        this.bot = bot;
        this.userStateService = userStateService;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.CANCEL_DELETE.getName();
    }

    @Override
    protected State requiredState() {
        return State.DeleteTimestampConfirmation;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();

        userStateService.update(user, UserStatePatch.builder().state(State.None).stateData(StateData.empty()).build());
        context.setState(State.None);

        sendDefaultMenuMessage(bot, context, chatId, "Вы отменили удаление фиксации");
    }
}
