package org.hardsign.models.timestamps.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hardsign.models.SortField;
import org.hardsign.models.timestamps.TimestampField;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class FindTimestampsRequest {
    private long activityId;
    @Nullable
    private Instant from;
    @Nullable
    private Instant to;
    private int page;
    private int pageSize;
    @Nullable
    private List<SortField<TimestampField>> sortFields;
}
