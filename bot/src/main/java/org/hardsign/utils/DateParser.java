package org.hardsign.utils;

import org.hardsign.models.DateRange;
import org.hardsign.models.LocalDateRange;

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
                    "\\s*((?<hours>\\d{2}):(?<minutes>\\d{2})(:(?<seconds>\\d{2}))?)?");

    public Optional<LocalDateTime> parseDate(String dateText) {
        var matcher = datePattern.matcher(dateText.trim());
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
        return parseDateRangeLocal(text).map(x -> x.atZone(zone));
    }

    public Optional<LocalDateRange> parseDateRangeLocal(String text) {
        var parts = text.split("-");

        if (parts.length != 2) {
            return Optional.empty();
        }

        var dateParts = Arrays.stream(parts)
                .map(this::parseDate)
                .collect(Collectors.toList());

        var from = dateParts.get(0);
        var to = dateParts.get(1);
        if (from.isEmpty() || to.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new LocalDateRange(from.get(), to.get()));
    }

    private Optional<LocalDate> parseLocalDate(Matcher matcher) {
        try {
            var day = Integer.parseInt(matcher.group("day"));
            var month = Integer.parseInt(matcher.group("month"));
            var year = Integer.parseInt(matcher.group("year"));
            return Optional.of(LocalDate.of(year, month, day));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    private Optional<LocalTime> parseLocalTime(Matcher matcher) {
        try {
            var hours = matcher.group("hours");
            var minutes = matcher.group("minutes");
            var seconds = matcher.group("seconds");

            if (hours == null || minutes == null) {
                return Optional.of(LocalTime.of(0, 0));
            }

            var hour = Integer.parseInt(hours);
            var minute = Integer.parseInt(minutes);
            var second = seconds == null ? 0 : Integer.parseInt(seconds);

            return Optional.of(LocalTime.of(hour, minute, second));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }
}
