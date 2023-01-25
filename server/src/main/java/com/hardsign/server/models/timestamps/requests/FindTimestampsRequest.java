package com.hardsign.server.models.timestamps.requests;

import com.hardsign.server.models.timestamps.TimestampSortField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class FindTimestampsRequest {
    @Positive
    private long activityId;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;

    private int take = 1000;

    private int skip = 0;

    private TimestampSortField[] sortBy = null;
}
