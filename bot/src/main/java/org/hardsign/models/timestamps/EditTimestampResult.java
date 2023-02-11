package org.hardsign.models.timestamps;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public class EditTimestampResult {
    @Nullable
    private TimestampDto timestamp;
    private boolean conflicted;
}
