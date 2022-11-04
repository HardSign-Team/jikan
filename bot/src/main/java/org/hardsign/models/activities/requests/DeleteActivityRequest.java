package org.hardsign.models.activities.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hardsign.models.requests.BaseBotRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DeleteActivityRequest extends BaseBotRequest {
    private long activityId;
}
