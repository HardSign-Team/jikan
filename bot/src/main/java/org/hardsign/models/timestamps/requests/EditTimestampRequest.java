package org.hardsign.models.timestamps.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditTimestampRequest {
    private long timestampId;
    @Nullable
    private LocalDateTime start;
    @Nullable
    private LocalDateTime end;
}
