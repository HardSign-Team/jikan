package org.hardsign.handlers.keyboards.abstracts;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.UpdateContext;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.handlers.keyboards.KeyboardPressHandler;

public abstract class ConfirmationDeleteActivityPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    protected void handleNotFoundActivity(
            TelegramBot bot,
            Long chatId,
            UpdateContext context) {
        var noActivityMessage = "Произошла ошибка! Не нашли выбранную активность. Попробуйте заново (по-братски)";
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, noActivityMessage).replyMarkup(keyboard));
    }
}
