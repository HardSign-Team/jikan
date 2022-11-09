package com.hardsign.server.services.auth;

import com.hardsign.server.models.auth.JwtTokens;
import com.hardsign.server.models.users.User;
import io.jsonwebtoken.Claims;

import java.util.Optional;

public interface AuthService {
    JwtTokens login(User user);

    boolean verifyPassword(User user, String password);

    Optional<Claims> getRefreshClaims(String refreshToken);

    String generateAccessToken(User user);

    JwtTokens refresh(User user);

    boolean verifyToken(String login, String refreshToken);
}
