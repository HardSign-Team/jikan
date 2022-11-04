package com.hardsign.server.models.activities.requests;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class PatchActivityRequest {

    private long id;

    @Nullable
    private String name;
}
