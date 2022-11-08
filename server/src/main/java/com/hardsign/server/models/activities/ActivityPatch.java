package com.hardsign.server.models.activities;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class ActivityPatch {
    @Nullable
    private String name;

    public ActivityPatch(@Nullable String name) {
        this.name = name;
    }

    public Activity apply(Activity activity) {
        return new Activity(activity.getId(), activity.getUserId(), name);
    }
}
