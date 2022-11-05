package org.hardsign.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.services.keyboardPressHandlers.ActivitiesPressHandler;
import org.hardsign.services.keyboardPressHandlers.ButtonNames;
import org.hardsign.services.keyboardPressHandlers.KeyboardPressHandler;

import java.util.*;
import java.util.logging.Logger;

public class UpdateListenerImpl implements UpdatesListener {
    private static final Logger LOGGER = Logger.getLogger(UpdateListenerImpl.class.getName());
    private final JikanApiClient jikanApiClient;
    private final TelegramBot bot;
    private final Map<String, KeyboardPressHandler> keyboardHandlers = new HashMap<>();

    public UpdateListenerImpl(JikanApiClient jikanApiClient, TelegramBot bot) {
        this.jikanApiClient = jikanApiClient;
        this.bot = bot;
        keyboardHandlers.put(ButtonNames.ACTIVITIES.getName(), new ActivitiesPressHandler(bot));
    }

    @Override
    public int process(List<Update> list) {
        return list.stream()
                .map(this::process)
                .reduce((x, y) -> y).map(Update::updateId)
                .orElse(CONFIRMED_UPDATES_ALL);
    }

    private Update process(Update update) {
        var content = update.message().text();
        if (Objects.equals(content, ""))
            return update;
        var parts = content.trim().split(" ");
        var command = parts[0];

        if (keyboardHandlers.containsKey(command)) {
            keyboardHandlers.get(command).handle(update);
            return update;
        }

        if ("/start".equals(command)) {
            handleStart(update);
        }
        return update;
    }

    private void handleStart(Update update) {
        var chatId = update.message().chat().id();
        var replyMarkup = new ReplyKeyboardMarkup(
        new KeyboardButton[] {
                new KeyboardButton(ButtonNames.ACTIVITIES.getName()),
        });
        var request = new SendMessage(chatId, "Choose your variant or /start")
                .replyMarkup(replyMarkup);
        sendMessage(request);
    }

    private void sendMessage(SendMessage request) {
        logError(bot.execute(request));
    }

    private void logError(BaseResponse response) {
        if (!response.isOk()) {
            LOGGER.severe(response.description());
        }
    }
}