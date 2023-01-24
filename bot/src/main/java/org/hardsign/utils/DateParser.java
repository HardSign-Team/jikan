package org.hardsign.utils;

import org.hardsign.models.DateRange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DateParser {
    private static final Pattern datePattern = Pattern.compile(
            "(?<day>\\d{1,2})\\.(?<month>\\d{1,2})\\.(?<year>\\d{4})" +
                    "\\s*((?<hours>\\d{2}):(?<minutes>\\d{2}):(?<seconds>\\d{2}))?");

    public Optional<LocalDateTime> parseDate(String dateText) {
        var matcher = datePattern.matcher(dateText);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        var date = parseLocalDate(matcher);
        var time = parseLocalTime(matcher);

        if (date.isEmpty() || time.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(LocalDateTime.of(date.get(), time.get()));
    }

    public Optional<DateRange> parseDateRange(String text, ZoneId zone) {
        var parts = text.split("-");

        if (parts.length != 2) {
            return Optional.empty();
        }

        var p = Arrays.stream(parts)
                .map(this::parseDate)
                .map(x -> x.map(y -> y.atZone(zone).toInstant()))
                .collect(Collectors.toList());

        var from = p.get(0);
        var to = p.get(1);
        if (from.isEmpty() || to.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new DateRange(from.get(), to.get()));
    }

    private Optional<LocalDate> parseLocalDate(Matcher matcher) {
        try {
            var day = parseGroupInt(matcher, "day");
            var month = parseGroupInt(matcher, "month");
            var year = parseGroupInt(matcher, "year");
            return Optional.of(LocalDate.of(year, month, day));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    private Optional<LocalTime> parseLocalTime(Matcher matcher) {
        try {
            var hour = parseGroupInt(matcher, "hours");
            var minute = parseGroupInt(matcher, "minutes");
            var second = parseGroupInt(matcher, "seconds");
            return Optional.of(LocalTime.of(hour, minute, second));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        } catch (IllegalArgumentException ignored) {
            return Optional.of(LocalTime.of(0, 0));
        }
    }

    private int parseGroupInt(Matcher matcher, String group) {
        return Integer.parseInt(matcher.group(group));
    }
}
