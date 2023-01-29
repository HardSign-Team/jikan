package org.hardsign.models.users;

public enum State {
    None,
    CreateActivityName,
    DeleteActivityConfirmation,
    SelectCustomDateRangeStatistics,
    AddTimestampDateRange,
    SelectCustomDateRangeTimestamps,
    DeleteTimestampConfirmation,
    EditTimestamp;

    public boolean isDefault() {
        return this == None;
    }
}
