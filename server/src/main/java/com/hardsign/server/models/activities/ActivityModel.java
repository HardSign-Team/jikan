package com.hardsign.server.models.activities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityModel {
    private final long id;
    private final long userId;
    private final String name;
}

