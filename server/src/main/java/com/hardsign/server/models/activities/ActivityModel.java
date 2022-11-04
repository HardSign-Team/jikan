package com.hardsign.server.models.activities;

import lombok.Data;

@Data
public class ActivityModel {

    private final long id;
    private final long userId;
    private final String name;

    public ActivityModel(long id, long userId, String name) {
        this.id = id;
        this.userId = userId;
        this.name = name;
    }
}

