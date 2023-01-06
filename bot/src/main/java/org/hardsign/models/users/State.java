package org.hardsign.models.users;

public enum State {
    None,
    CreateActivityName,
    DeleteActivityConfirmation,
    SelectCustomDateRangeStatistics;

    public boolean isDefault() {
        return this == None;
    }
}
