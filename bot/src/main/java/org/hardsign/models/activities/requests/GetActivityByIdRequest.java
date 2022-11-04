package org.hardsign.models.activities.requests;

import lombok.*;
import org.hardsign.models.requests.BaseBotRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GetActivityByIdRequest extends BaseBotRequest {
    private long activityId;
}
