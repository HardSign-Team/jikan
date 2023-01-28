package org.hardsign.utils;

public class MessagesHelper {
    private MessagesHelper() {}

    public static String createIncorrectDateRangeFormatMessage() {
        return "Неверный формат. Попробуйте еще раз.\n" +
                "Правильный формат: " + "\n" +
                Hints.DATE_FORMAT_HINT + "\n" +
                Hints.DATE_RANGE_FORMAT_HINT;
    }
}
