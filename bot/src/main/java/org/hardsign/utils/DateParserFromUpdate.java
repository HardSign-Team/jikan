package org.hardsign.utils;

import com.pengrad.telegrambot.model.Update;
import org.hardsign.models.DateRange;

import java.util.Optional;

public class DateParserFromUpdate {

    private final DateParser parser;
    private final TimezoneHelper timezoneHelper;

    public DateParserFromUpdate(DateParser parser, TimezoneHelper timezoneHelper) {
        this.parser = parser;
        this.timezoneHelper = timezoneHelper;
    }

    public Optional<DateRange> parseDateRange(Update update) {
        var zone = timezoneHelper.getZone(update.message().location());

        var text = update.message().text();
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }

        return parser.parseDateRange(text, zone);
    }
}
