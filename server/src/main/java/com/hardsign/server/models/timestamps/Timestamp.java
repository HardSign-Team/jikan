package com.hardsign.server.models.timestamps;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class Timestamp {
    private long id;
    private long activityId;
    private ZonedDateTime start;
    @Nullable
    private ZonedDateTime end;

    public Timestamp(long activityId, ZonedDateTime start) {
        this(0, activityId, start, null);
    }
}
