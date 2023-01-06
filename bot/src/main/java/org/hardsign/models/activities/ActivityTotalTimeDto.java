package org.hardsign.models.activities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityTotalTimeDto {
    private Instant from;
    private Instant to;
    private long durationSec;
}
