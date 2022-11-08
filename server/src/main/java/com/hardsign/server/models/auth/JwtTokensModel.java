package com.hardsign.server.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokensModel {
    private String accessToken;
    private String refreshToken;
}
