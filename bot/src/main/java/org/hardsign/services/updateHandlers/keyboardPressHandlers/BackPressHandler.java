package org.hardsign.services.updateHandlers.keyboardPressHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;

import java.util.Objects;

public class BackPressHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;

    public BackPressHandler(TelegramBot bot, JikanApiClient jikanApiClient) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
    }

    @Override
    public void handle(Update update, UpdateContext context) throws Exception {
        if (!context.isRegistered())
            return;

        var user = update.message().from();
        if (user.isBot())
            return;

        if (!context.getState().isDefault())
            return;

        var message = update.message().text();
        if (!Objects.equals(message, ButtonNames.BACK.getName()))
            return;

        var chatId = update.message().chat().id();
        bot.execute(new SendMessage(chatId, "Выберите действие")
                            .replyMarkup(KeyboardFactory.createMainMenu(context, jikanApiClient)));
    }
}
