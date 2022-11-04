package org.hardsign.models.activities.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hardsign.models.requests.BaseBotRequest;

@AllArgsConstructor
@NoArgsConstructor
public class UpdateActivityRequest extends BaseBotRequest {
    private String name;
}