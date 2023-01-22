package com.hardsign.server.models.timestamps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimestampPatch {
    @Nullable
    private Instant start;

    @Nullable
    private Instant end;

    public Timestamp apply(Timestamp timestamp) {
        var newStart = Optional.ofNullable(start).orElse(timestamp.getStart());
        var newEnd = Optional.ofNullable(end).orElse(timestamp.getEnd());

        return timestamp.withDateRange(newStart, newEnd);
    }
}
