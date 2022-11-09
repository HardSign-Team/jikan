package org.hardsign.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.jetbrains.annotations.Nullable;

public abstract class BaseUpdateHandler implements UpdateHandler {
    @Override
    public void handle(Update update, UpdateContext context) throws Exception {
        var user = update.message().from();
        if (shouldNotBot() && user.isBot())
            return;

        if (shouldBeRegistered() && !context.isRegistered())
            return;

        if (shouldRequireState() && requiredState() != context.getState())
            return;

        if (!checkText(update.message().text()))
            return;

        handleInternal(user, update, context);
    }

    protected abstract void handleInternal(User user, Update update, UpdateContext context)
            throws Exception;

    protected boolean checkText(@Nullable String messageText) {
        return true;
    }

    protected boolean shouldNotBot() {
        return true;
    }

    protected boolean shouldBeRegistered() {
        return true;
    }

    protected boolean shouldRequireState() {
        return true;
    }

    protected State requiredState() {
        return State.None;
    }

    protected void handleNoCurrentActivity(
            TelegramBot bot,
            UpdateContext context, Long chatId) {
        var text = "Вы не выбрали активность. Можете сделать это через главное меню.";
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    protected void sendDefaultMenuMessage(TelegramBot bot,
                                          UpdateContext context,
                                          Long chatId,
                                          String text) {
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }
}
