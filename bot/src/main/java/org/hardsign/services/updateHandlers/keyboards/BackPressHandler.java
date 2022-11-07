package org.hardsign.services.updateHandlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.services.updateHandlers.BaseTextUpdateHandler;

public class BackPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;

    public BackPressHandler(TelegramBot bot, JikanApiClient jikanApiClient) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var keyboard = KeyboardFactory.createMainMenu(context, jikanApiClient);
        bot.execute(new SendMessage(chatId, "Выберите действие").replyMarkup(keyboard));
    }

    @Override
    protected String expectedText() {
        return ButtonNames.BACK.getName();
    }
}
