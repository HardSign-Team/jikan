package org.hardsign.models.timestamps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hardsign.models.TimestampZonedDateRange;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimestampDto {
    private long id;
    private long activityId;
    private Instant start;
    @Nullable
    private Instant end;

    public static int compareAscending(TimestampDto x, TimestampDto y) {
        return x.getStart().compareTo(y.getStart());
    }

    public TimestampZonedDateRange getDateRange(ZoneId zoneId) {
        var from = start.atZone(zoneId);
        var to = Optional.ofNullable(end).map(x -> x.atZone(zoneId)).orElse(null);
        return new TimestampZonedDateRange(from, to);
    }
}