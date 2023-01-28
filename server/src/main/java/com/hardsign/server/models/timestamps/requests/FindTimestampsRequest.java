package com.hardsign.server.models.timestamps.requests;

import com.hardsign.server.models.SortField;
import com.hardsign.server.models.timestamps.TimestampField;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Positive;
import java.time.Instant;
import java.util.List;

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

    private int page = 0;

    private int pageSize = 1000;

    private List<SortField<TimestampField>> sortFields = null;
}
