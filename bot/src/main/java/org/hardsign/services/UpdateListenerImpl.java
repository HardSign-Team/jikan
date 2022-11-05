package org.hardsign.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.services.auth.Authorizer;
import org.hardsign.services.updateHandlers.UpdateHandler;
import org.hardsign.services.updateHandlers.commands.StartCommandHandler;
import org.hardsign.services.updateHandlers.keyboardPressHandlers.ActivitiesPressHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class UpdateListenerImpl implements UpdatesListener {
    private static final Logger LOGGER = Logger.getLogger(UpdateListenerImpl.class.getName());
    private final JikanApiClient jikanApiClient;
    private final Authorizer authorizer;
    private final TelegramBot bot;
    private final List<UpdateHandler> updateHandlers = new ArrayList<>();

    public UpdateListenerImpl(JikanApiClient jikanApiClient, Authorizer authorizer, TelegramBot bot) {
        this.jikanApiClient = jikanApiClient;
        this.authorizer = authorizer;
        this.bot = bot;
        updateHandlers.add(new StartCommandHandler(bot, jikanApiClient));

        updateHandlers.add(new ActivitiesPressHandler(bot));
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

        for (var handler : updateHandlers) {
            try {
                handler.handle(update);
            } catch (Exception e) {
                LOGGER.severe(e.getMessage());
            }
        }

        return update;
    }
}