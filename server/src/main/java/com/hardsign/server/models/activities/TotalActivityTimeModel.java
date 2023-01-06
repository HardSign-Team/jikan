package com.hardsign.server.models.activities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class TotalActivityTimeModel {
    private Instant from;
    private Instant to;
    private Long durationSec;
}
