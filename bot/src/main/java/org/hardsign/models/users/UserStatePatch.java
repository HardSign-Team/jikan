package org.hardsign.models.users;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
@Builder
public class UserStatePatch {
    @Nullable
    private State state;
    @Nullable
    private Long activityId;
    @Nullable
    private Long deleteActivityId;
    @Nullable
    private StateData stateData;
}
