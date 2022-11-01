package com.hardsign.server.models.timestamps.requests;

public class StopTimestampRequest {
    private long activityId;

    public StopTimestampRequest(long activityId) {
        this.activityId = activityId;
    }

    public StopTimestampRequest() {
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }
}
