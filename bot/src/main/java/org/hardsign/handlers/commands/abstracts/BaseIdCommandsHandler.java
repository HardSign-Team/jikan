package org.hardsign.handlers.commands.abstracts;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.handlers.BaseUpdateHandler;
import org.hardsign.models.UpdateContext;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public abstract class BaseIdCommandsHandler extends BaseUpdateHandler {
    @Nullable
    private Pattern pattern;

    protected abstract String getPrefix();

    protected abstract void handleInternal(User user, Long id, Update update, UpdateContext context)
            throws Exception;

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var text = update.message().text();
        if (text == null)
            return;

        var matcher = getPattern().matcher(text);
        if (!matcher.matches()) {
            return;
        }

        var id = Long.parseLong(matcher.group(1));
        handleInternal(user, id, update, context);
    }

    private Pattern getPattern() {
        return pattern != null ? pattern : (pattern = Pattern.compile(getPrefix() + "(\\d+)"));
    }
}