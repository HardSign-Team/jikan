package org.hardsign.utils;

import java.time.Duration;

public class TimeFormatter {
    public String format(Duration duration) {
        var years = duration.toDaysPart();
        var days = duration.toDays();
        var hours = duration.toHoursPart();
        var minutes = duration.toMinutesPart();

        var sb = new StringBuilder();

        if (years > 0)
            sb.append(years).append("лет ");
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
