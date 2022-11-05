package org.hardsign.models.activities.requests;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetActivityByIdRequest {
    private long activityId;
}
