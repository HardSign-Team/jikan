package com.hardsign.server.models.activities;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class ActivityPatch {
    @Nullable
    private String name;
}
