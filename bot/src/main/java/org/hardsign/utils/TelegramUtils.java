package org.hardsign.utils;

import com.pengrad.telegrambot.model.request.ParseMode;

public class TelegramUtils {
    public static final ParseMode PARSE_MODE = ParseMode.HTML;

    private TelegramUtils() {
    }

    public static String bold(String value) {
        return "<b>" + value + "</b>";
    }
}
