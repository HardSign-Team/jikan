package com.hardsign.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends ResponseStatusException {

    public ConflictException(@Nullable String error) {
        super(HttpStatus.CONFLICT, error);
    }
}
