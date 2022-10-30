package com.hardsign.server.services;

import com.hardsign.server.models.auth.JwtRequest;
import com.hardsign.server.models.auth.JwtResponse;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    public AuthService(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    public JwtResponse login(JwtRequest authRequest) throws AuthException {
        var user = userService.getUser(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        var password = authRequest.getPassword(); //TODO (lunev.d) add password hashing
        if (user.HashedPassword.equals(password)) {
            var accessToken = jwtProvider.generateAccessToken(user);
            var refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.Login, refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            var claims = jwtProvider.getRefreshClaims(refreshToken);
            var login = claims.getSubject();
            var saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                var user = userService.getUser(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                var accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            var claims = jwtProvider.getRefreshClaims(refreshToken);
            var login = claims.getSubject();
            var saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                var user = userService.getUser(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                var accessToken = jwtProvider.generateAccessToken(user);
                var newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.Login, newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }
}
