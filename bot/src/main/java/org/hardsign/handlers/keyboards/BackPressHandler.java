package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.services.users.UserStateService;

public class BackPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final UserStateService userStateService;

    public BackPressHandler(TelegramBot bot, UserStateService userStateService) {
        this.bot = bot;
        this.userStateService = userStateService;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();

        userStateService.with(context).setState(user, State.None);

        var text = "Выберите действие";
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    @Override
    protected String expectedText() {
        return ButtonNames.BACK.getName();
    }

    @Override
    protected boolean shouldRequireState() {
        return false;
    }

    @Override
    protected State requiredState() {
        return super.requiredState();
    }
}
