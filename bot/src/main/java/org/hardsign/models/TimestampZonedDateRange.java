package org.hardsign.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class TimestampZonedDateRange {
    private ZonedDateTime from;
    @Nullable
    private ZonedDateTime to;
}
