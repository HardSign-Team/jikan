package org.hardsign.models.users;

public enum State {
    None,
    CreateActivityName,
    DeleteActivityConfirmation;

    public boolean isDefault() {
        return this == None;
    }
}
