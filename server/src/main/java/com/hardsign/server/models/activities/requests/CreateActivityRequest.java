package com.hardsign.server.models.activities.requests;


import com.sun.istack.Nullable;

public class CreateActivityRequest {

    @Nullable
    private String name;

    public CreateActivityRequest(String name) {
        this.name = name;
    }

    public CreateActivityRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

