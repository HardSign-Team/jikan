package org.hardsign.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class DateRange {
    private Instant from;
    private Instant to;
}
