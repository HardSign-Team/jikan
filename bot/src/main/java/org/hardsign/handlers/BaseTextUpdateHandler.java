package org.hardsign.handlers;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class BaseTextUpdateHandler extends BaseUpdateHandler {
    @Override
    protected boolean checkText(@Nullable String messageText) {
        return messageText != null && Objects.equals(expectedText(), messageText.trim());
    }

    protected abstract String expectedText();
}
