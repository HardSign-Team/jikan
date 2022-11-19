package org.hardsign.models.activities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hardsign.models.timestamps.TimestampDto;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityOverviewDto {
    private long id;
    private long userId;
    private String name;
    @Nullable
    private TimestampDto activeTimestamp;
}
