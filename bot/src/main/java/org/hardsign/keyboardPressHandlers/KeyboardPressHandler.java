package org.hardsign.keyboardPressHandlers;

import com.pengrad.telegrambot.model.Update;

public interface KeyboardPressHandler {
    void handle(Update update);
}