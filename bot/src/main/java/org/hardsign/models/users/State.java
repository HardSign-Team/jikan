package org.hardsign.models.users;

public enum State {
    None,
    CreateActivityName,
    DeleteActivityConfirmation,
    SelectCustomDateRangeStatistics,
    AddTimestampDateRange,
    SelectCustomDateRangeTimestamps,
    DeleteTimestampConfirmation;

    public boolean isDefault() {
        return this == None;
    }
}
