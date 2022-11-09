package com.hardsign.server.models.timestamps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimestampModel {
    private long id;
    private long activityId;
    private ZonedDateTime start;
    @Nullable
    private ZonedDateTime end;
}
