package com.hardsign.server.models.users;

import com.hardsign.server.models.activities.Activity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    private final long id;
    private final String name;
    private final String login;
    private final String hashedPassword;
    private final UserRole role;

    public boolean hasAccess(Activity activity) {
        return activity.getUserId() == id;
    }

    public boolean isService() {
        return role == UserRole.SERVICE;
    }
}
