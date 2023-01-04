package com.hardsign.server.models.timestamps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimestampModel {
    private long id;
    private long activityId;
    private Instant start;
    @Nullable
    private Instant end;
}
