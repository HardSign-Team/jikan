package com.hardsign.server.models.timestamps.requests;

public class StartTimestampRequest {
    private long activityId;

    public StartTimestampRequest(long activityId) {
        this.activityId = activityId;
    }

    public StartTimestampRequest() {}

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }
}

