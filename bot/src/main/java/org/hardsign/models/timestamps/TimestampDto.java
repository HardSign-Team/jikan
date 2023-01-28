package org.hardsign.models.timestamps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

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
}