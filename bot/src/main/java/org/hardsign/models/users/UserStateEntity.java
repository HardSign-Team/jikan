package org.hardsign.models.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = {"userId"})
@AllArgsConstructor
@NoArgsConstructor
public class UserStateEntity {
    private long userId;
    private UserState state;
    private long activityId;
}
