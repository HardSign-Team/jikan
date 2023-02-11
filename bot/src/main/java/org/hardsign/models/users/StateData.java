package org.hardsign.models.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateData {
    private long activityId;
    private long timestampId;

    public static StateData empty() {
        return new StateData();
    }

    public static StateData fromTimestamp(long timestampId) {
        return StateData.builder().timestampId(timestampId).build();
    }
}
