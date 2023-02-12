package org.hardsign.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LocalDateRange {
    private LocalDateTime from;
    private LocalDateTime to;

    public DateRange atZone(ZoneId zoneId) {
        return new DateRange(from.atZone(zoneId).toInstant(), to.atZone(zoneId).toInstant());
    }
}
