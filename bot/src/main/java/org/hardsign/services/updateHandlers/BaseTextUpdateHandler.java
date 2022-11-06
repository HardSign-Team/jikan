package org.hardsign.services.updateHandlers;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class BaseTextUpdateHandler extends BaseUpdateHandler {
    @Override
    protected boolean checkText(@Nullable String messageText) {
        return messageText != null && Objects.equals(expectedText(), messageText);
    }

    protected abstract String expectedText();
}
