package com.hardsign.server.models.timestamps;

import java.util.Date;

public class TimestampModel {
    public String ActivityId;;
    public String Id;
    public Date Start;
    public Date End;

    public TimestampModel(String id, String activityId, Date start, Date end) {
        this.Id = id;
        this.ActivityId = activityId;
        this.Start = start;
        this.End = end;
    }
}
