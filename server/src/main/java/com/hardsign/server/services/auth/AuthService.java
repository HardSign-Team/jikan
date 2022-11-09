package com.hardsign.server.services.auth;

import com.hardsign.server.models.auth.JwtTokens;
import com.hardsign.server.models.users.UserEntity;
import io.jsonwebtoken.Claims;

import java.util.Optional;

public interface AuthService {
    JwtTokens login(UserEntity user);

    boolean verifyPassword(UserEntity user, String password);

    Optional<Claims> getRefreshClaims(String refreshToken);

    String generateAccessToken(UserEntity user);

    JwtTokens refresh(UserEntity user);

    boolean verifyToken(String login, String refreshToken);
}
