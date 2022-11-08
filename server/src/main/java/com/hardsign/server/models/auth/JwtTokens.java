package com.hardsign.server.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokens {
    private String accessToken;
    private String refreshToken;
}


