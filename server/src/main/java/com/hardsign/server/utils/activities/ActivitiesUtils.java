package com.hardsign.server.utils.activities;

import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.users.User;

import java.util.function.Predicate;

public class ActivitiesUtils {
    public static Predicate<? super Activity> isOwnedBy(User user) {
        return user::hasAccess; // todo: (tebaikin) 04.11.2022 inline
    }
}
