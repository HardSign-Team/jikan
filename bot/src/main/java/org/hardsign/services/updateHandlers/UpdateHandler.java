package org.hardsign.services.updateHandlers;

import com.pengrad.telegrambot.model.Update;
import org.hardsign.models.UpdateContext;

public interface UpdateHandler {
    void handle(Update update, UpdateContext context) throws Exception;
}