package com.hardsign.server.models.activities;

import java.util.UUID;

public class ActivityModel {
    public String UserId;
    public String Id;
    public String Name;

    public ActivityModel(String userId, String id, String name) {
        this.UserId = userId;
        this.Id = id;
        this.Name = name;
    }
}
