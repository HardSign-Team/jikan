package com.hardsign.server.models.timestamps;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class FindTimestampsArgs {
    @NotNull private long activityId;
    @NotNull private Instant from;
    @NotNull private Instant to;
    @NotNull private int skip;
    @NotNull private int take;
    @NotNull private TimestampSortField[] sortBy;
}