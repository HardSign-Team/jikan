package com.hardsign.server.models.auth;

import lombok.Data;

@Data
public class RefreshJwtRequest {
    private String refreshToken;
}

