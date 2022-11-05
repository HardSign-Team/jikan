package org.hardsign.models.users;

public enum UserState {
    None,
    CreateActivityName,
    ;

    public boolean isDefault() {
        return this == None;
    }
}
