package org.hardsign.models.users;

public enum State {
    None,
    CreateActivityName,
    DeleteActivityConfirmation,
    SelectCustomDateRangeStatistics,
    AddTimestampDateRange,
    SelectCustomDateRangeTimestamps,
    ;

    public boolean isDefault() {
        return this == None;
    }
}
