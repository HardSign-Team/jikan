package com.hardsign.server.models.errors;

import lombok.Data;

@Data
public class ApiError {
    private final String description;

    public ApiError(String description) {
        this.description = description;
    }
}
