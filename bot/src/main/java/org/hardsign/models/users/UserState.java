package org.hardsign.models.users;

public enum UserState {
    None,
    CreateActivityName,
    DeleteActivityConfirmation;

    public boolean isDefault() {
        return this == None;
    }
}
