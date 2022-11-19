package com.hardsign.server.models.activities;

import com.hardsign.server.models.timestamps.TimestampModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class ActivityOverviewModel {
    private final long id;
    private final long userId;
    private final String name;
    @Nullable
    private final TimestampModel activeTimestamp;
}
