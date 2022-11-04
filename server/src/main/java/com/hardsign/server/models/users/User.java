package com.hardsign.server.models.users;

import com.hardsign.server.models.activities.Activity;

public class User {
    private final long id;
    private final String name;
    private final String login;

    public User(long id, String name, String login) {
        this.id = id;
        this.name = name;
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public boolean hasAccess(Activity activity) {
        return activity.getUserId() == id;
    }
}
