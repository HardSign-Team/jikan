package org.hardsign.models.activities;

import java.util.UUID;

public class ActivityDto {
    private final UUID id;
    private final String name;

    public ActivityDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
