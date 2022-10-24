package org.hardsign;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class UpdateListenerImpl implements UpdatesListener {
    private static final Logger LOGGER = Logger.getLogger(UpdateListenerImpl.class.getName());
    private final TelegramBot bot;

    public UpdateListenerImpl(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public int process(List<Update> list) {
        Update lastUpdate = null;

        for (var item : list) {
            lastUpdate = item;
            try {
                process(item);
            } catch (Exception e) {
                LOGGER.severe(e.getMessage());
            }
        }

        if (lastUpdate != null) {
            return lastUpdate.updateId();
        }

        return CONFIRMED_UPDATES_ALL;
    }

    private void process(Update update) throws Exception {
        var content = update.message().text();
        if (Objects.equals(content, ""))
            return;
        long chatId = update.message().chat().id();
        var response = bot.execute(new SendMessage(chatId, "Hello!"));
        if (response.isOk()) {
            throw new Exception(response.toString());
        }
    }
}
