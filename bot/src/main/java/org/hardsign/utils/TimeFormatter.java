package org.hardsign.utils;

import java.time.Duration;

public class TimeFormatter {
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
