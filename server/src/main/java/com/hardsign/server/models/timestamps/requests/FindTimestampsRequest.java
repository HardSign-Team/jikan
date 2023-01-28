package com.hardsign.server.models.timestamps.requests;

import com.hardsign.server.models.timestamps.TimestampSortField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Positive;
import java.time.Instant;

@Data
public class FindTimestampsRequest {
    @Positive
    private long activityId;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant from;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant to;

    private int skip = 0;

    private int take = 1000;

    private TimestampSortField[] sortFields = null;
}
