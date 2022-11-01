package com.hardsign.server.models.activities.requests;

import net.bytebuddy.utility.nullability.MaybeNull;
import org.springframework.lang.Nullable;

public class PatchActivityRequest {
    private long id;
    @Nullable
    private String name;

    public PatchActivityRequest(long id, @MaybeNull String name) {
        this.id = id;
        this.name = name;
    }

    @MaybeNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
