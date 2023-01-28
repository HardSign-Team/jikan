package com.hardsign.server.models.timestamps.requests;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Positive;
import java.time.Instant;

@Data
public class EditTimestampRequest {
    @Positive
    private long timestampId;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant start;

    @Nullable
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant end;
}
