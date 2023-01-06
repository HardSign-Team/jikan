package org.hardsign.models.activities.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetActivityTotalTimeRequest {
    private long activityId;
    private Instant from;
    private Instant to;
}
