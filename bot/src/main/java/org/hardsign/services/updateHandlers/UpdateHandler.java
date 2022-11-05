package org.hardsign.services.updateHandlers;

import com.pengrad.telegrambot.model.Update;

public interface UpdateHandler {
    void handle(Update update);
}
