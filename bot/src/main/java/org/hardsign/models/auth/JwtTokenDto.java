package org.hardsign.models.auth;

import lombok.Data;

@Data
public class JwtTokenDto {
    private String accessToken;
    private String refreshToken;
}
