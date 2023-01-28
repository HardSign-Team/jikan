package com.hardsign.server.models.timestamps;

import com.hardsign.server.models.SortField;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class FindTimestampsArgs {
    @NotNull private long activityId;
    @NotNull private Instant from;
    @NotNull private Instant to;
    @NotNull private int page;
    @NotNull private int pageSize;
    @NotNull private List<SortField<TimestampField>> sortBy;
}