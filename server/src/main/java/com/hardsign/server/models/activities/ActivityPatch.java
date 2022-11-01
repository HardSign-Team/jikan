package com.hardsign.server.models.activities;

import org.springframework.lang.Nullable;

public class ActivityPatch {
    @Nullable
    private String name;

    public ActivityPatch(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }
}
