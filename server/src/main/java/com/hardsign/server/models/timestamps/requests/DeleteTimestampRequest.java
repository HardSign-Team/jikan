package com.hardsign.server.models.timestamps.requests;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class DeleteTimestampRequest {
    @Positive
    private long timestampId;
}
