package org.hardsign.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.models.auth.TelegramUserAuthMeta;
import org.hardsign.services.auth.Authorizer;
import org.hardsign.services.keyboardPressHandlers.ActivitiesPressHandler;
import org.hardsign.services.keyboardPressHandlers.ButtonNames;
import org.hardsign.services.keyboardPressHandlers.KeyboardPressHandler;

import java.util.*;
import java.util.logging.Logger;

public class UpdateListenerImpl implements UpdatesListener {
    private static final Logger LOGGER = Logger.getLogger(UpdateListenerImpl.class.getName());
    private final JikanApiClient jikanApiClient;
    private final Authorizer authorizer;
    private final TelegramBot bot;
    private final Map<String, KeyboardPressHandler> keyboardHandlers = new HashMap<>();

    public UpdateListenerImpl(JikanApiClient jikanApiClient, Authorizer authorizer, TelegramBot bot) {
        this.jikanApiClient = jikanApiClient;
        this.authorizer = authorizer;
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
        var user = update.message().from();
        if (!user.isBot())
            registerUser(user);
    }

    private void registerUser(User user) {
        var meta = new TelegramUserAuthMeta(user.id(), user.username(), user.firstName());
        try {
            authorizer.createUser(meta);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
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