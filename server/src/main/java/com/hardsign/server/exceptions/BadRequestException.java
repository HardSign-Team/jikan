package com.hardsign.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException  {

    public BadRequestException(@Nullable String reason) {
        super(HttpStatus.BAD_REQUEST, reason, null);
    }
}

