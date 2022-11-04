package com.hardsign.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ForbiddenException extends ResponseStatusException {

    public ForbiddenException() {
        this("User not found.");
    }

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
