package com.hardsign.server.models.activities;

public class ActivityModel {
    public long Id;
    public long UserId;
    public String Name;

    public ActivityModel(long id, long userId, String name) {
        this.Id = id;
        this.UserId = userId;
        this.Name = name;
    }
}

