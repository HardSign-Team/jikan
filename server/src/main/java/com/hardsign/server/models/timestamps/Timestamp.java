package com.hardsign.server.models.timestamps;

import net.bytebuddy.utility.nullability.MaybeNull;

import java.util.Date;

public class Timestamp {
    private long id;
    private long activityId;
    private Date start;
    @MaybeNull
    private Date end;

    public Timestamp(long id, long activityId, Date start, @MaybeNull Date end) {
        this.id = id;
        this.activityId = activityId;
        this.start = start;
        this.end = end;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @MaybeNull
    public Date getEnd() {
        return end;
    }

    public void setEnd(@MaybeNull Date end) {
        this.end = end;
    }
}
