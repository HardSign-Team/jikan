package com.hardsign.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
    public NotFoundException() {
        this("Not found.");
    }

    public NotFoundException(@Nullable String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}

