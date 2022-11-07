package org.hardsign.models.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = {"userId"})
@AllArgsConstructor
@NoArgsConstructor
public class UserState {
    private long userId;
    private State state;
    private long activityId;
    private long deleteActivityId;
}

