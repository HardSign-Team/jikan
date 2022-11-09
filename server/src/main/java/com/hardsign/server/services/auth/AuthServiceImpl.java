package com.hardsign.server.services.auth;

import com.hardsign.server.models.auth.JwtTokens;
import com.hardsign.server.models.users.User;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final Map<String, String> refreshStorage = new HashMap<>(); // todo: (tebaikin) 08.11.2022 to repository
    private final JwtProvider jwtProvider;
    private final PasswordService passwordService;

    public AuthServiceImpl(JwtProvider jwtProvider, PasswordService passwordService) {
        this.jwtProvider = jwtProvider;
        this.passwordService = passwordService;
    }

    @Override
    public JwtTokens login(User user) {
        return refresh(user);
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        return passwordService.verifyHash(password, user.getHashedPassword());
    }

    @Override
    public Optional<Claims> getRefreshClaims(String refreshToken) {
        return jwtProvider.getRefreshClaims(refreshToken);
    }

    @Override
    public String generateAccessToken(User user) {
        return jwtProvider.generateAccessToken(user);
    }

    @Override
    public JwtTokens refresh(User user) {
        var accessToken = generateAccessToken(user);
        var newRefreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getLogin(), newRefreshToken);
        return new JwtTokens(accessToken, newRefreshToken);
    }

    @Override
    public boolean verifyToken(String login, String refreshToken) {
        return Objects.equals(refreshStorage.get(login), refreshToken);
    }
}
