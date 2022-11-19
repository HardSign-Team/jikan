package org.hardsign.services;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;

import java.util.logging.Logger;

public class BotExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(BotExceptionHandler.class.getName());
    @Override
    public void onException(TelegramException e) {
        LOGGER.severe("TelegramException on response: " + e.response()
                              + System.lineSeparator()
                              + "Message: " + e.getMessage());
    }
}
