package com.hardsign.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizedException extends ResponseStatusException {
    public UnauthorizedException() {
        this(null);
    }

    public UnauthorizedException(@Nullable String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
