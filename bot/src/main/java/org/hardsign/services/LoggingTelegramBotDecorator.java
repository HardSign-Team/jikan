package org.hardsign.services;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;

import java.io.IOException;
import java.util.logging.Logger;

public class LoggingTelegramBotDecorator extends TelegramBot {
    private final Logger logger;

    public LoggingTelegramBotDecorator(String botToken, Logger logger) {
        super(botToken);
        this.logger = logger;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> R execute(BaseRequest<T, R> request) {
        var response = super.execute(request);
        logResponse(response);
        return response;
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(T request, Callback<T, R> callback) {
        super.execute(request, new Callback<T, R>() {
            @Override
            public void onResponse(T t, R r) {
                logResponse(r);
                callback.onResponse(t, r);
            }

            @Override
            public void onFailure(T t, IOException e) {
                logError(e);
                callback.onFailure(t, e);
            }
        });
    }

    private void logError(IOException e) {
        logger.severe(e.getMessage());
    }

    private void logResponse(BaseResponse response) {
        if (!response.isOk()) {
            logger.severe(response.toString());
        }
    }
}
