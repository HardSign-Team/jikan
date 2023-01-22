package com.hardsign.server.models.timestamps;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Data
@AllArgsConstructor
public class Timestamp {
    private long id;
    private long activityId;
    private Instant start;
    @Nullable
    private Instant end;

    public Timestamp(long activityId, Instant start) {
        this(0, activityId, start, null);
    }

    public Timestamp(long activityId, Instant start, Instant end) {
        this(0, activityId, start, end);
    }

    public Timestamp withDateRange(Instant start, Instant end) {
        return new Timestamp(id, activityId, start, end);
    }
}
