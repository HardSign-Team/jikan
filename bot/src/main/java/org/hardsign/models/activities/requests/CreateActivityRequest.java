package org.hardsign.models.activities.requests;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hardsign.models.requests.BaseBotRequest;

@AllArgsConstructor
@NoArgsConstructor
public class CreateActivityRequest extends BaseBotRequest {
    private String name;
}

