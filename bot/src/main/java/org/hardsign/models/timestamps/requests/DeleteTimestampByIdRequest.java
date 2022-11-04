package org.hardsign.models.timestamps.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hardsign.models.requests.BaseBotRequest;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTimestampByIdRequest extends BaseBotRequest {
    private long activityId;
}
