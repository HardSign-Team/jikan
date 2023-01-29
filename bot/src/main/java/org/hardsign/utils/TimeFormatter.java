package org.hardsign.utils;

import org.hardsign.models.TimestampZonedDateRange;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TimeFormatter {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    public String format(TimestampZonedDateRange dateRange) {
        return format(dateRange.getFrom(), dateRange.getTo());
    }

    public String format(ZonedDateTime start, @Nullable ZonedDateTime end) {
        return format(start) + " — " + Optional.ofNullable(end).map(this::format).orElse("...");
    }

    public String format(ZonedDateTime date) {
        return date.format(dateTimeFormatter);
    }

    public String format(Duration duration) {
        var days = duration.toDaysPart();
        var hours = duration.toHoursPart();
        var minutes = duration.toMinutesPart();

        var sb = new StringBuilder();

        if (days > 0)
            sb.append(days).append("д. ");
        if (hours > 0)
            sb.append(hours).append("ч. ");
        if (minutes > 0)
            sb.append(minutes).append("мин.");

        var result =  sb.toString().trim();

        if (result.isBlank())
            return "совсем немножко";
        return result;
    }
}
