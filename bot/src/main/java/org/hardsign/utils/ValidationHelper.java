package org.hardsign.utils;

import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.users.UserDto;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ValidationHelper {
    @NotNull
    public static Boolean isOwnActivity(@Nullable UserDto user, ActivityDto activity) {
        return Optional.ofNullable(user)
                .map(x -> x.getId() == activity.getUserId())
                .orElse(false);
    }
}
