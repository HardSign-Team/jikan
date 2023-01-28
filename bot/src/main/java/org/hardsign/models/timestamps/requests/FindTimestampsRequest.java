package org.hardsign.models.timestamps.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hardsign.models.timestamps.TimestampSortField;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@AllArgsConstructor
public class FindTimestampsRequest {
    private long activityId;
    @Nullable
    private Instant from;
    @Nullable
    private Instant to;
    private int skip;
    private int take;
    @Nullable
    private TimestampSortField[] sortFields;

    public FindTimestampsRequest(long activityId) {
        this(activityId, null, null, 0, 15, null);
    }

    public FindTimestampsRequest(long activityId, Instant from, Instant to) {
        this(activityId, from, to, 0, 15, null);
    }
}
