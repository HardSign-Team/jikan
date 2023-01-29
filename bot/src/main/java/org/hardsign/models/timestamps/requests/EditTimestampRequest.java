package org.hardsign.models.timestamps.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditTimestampRequest {
    private long timestampId;
    @Nullable
    private Instant start;
    @Nullable
    private Instant end;
}
