package com.hardsign.server.utils.users;

import com.hardsign.server.exceptions.UnauthorizedException;
import com.hardsign.server.models.users.User;
import com.hardsign.server.services.user.CurrentUserProvider;

public class UserUtils {
    public static User getUserOrThrow(CurrentUserProvider currentUserProvider) {
        return currentUserProvider.getCurrentUser()
                .orElseThrow(UnauthorizedException::new);
    }
}
