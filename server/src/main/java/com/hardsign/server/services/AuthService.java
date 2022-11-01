package com.hardsign.server.services;

import com.hardsign.server.models.auth.JwtRequest;
import com.hardsign.server.models.auth.JwtResponse;
import com.hardsign.server.models.users.UserEntity;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final PasswordService passwordService;

    public AuthService(UserService userService, JwtProvider jwtProvider, PasswordService passwordService) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordService = passwordService;
    }

    public JwtResponse login(JwtRequest authRequest) throws AuthException {
        var user = userService.getUser(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("User not found"));

        var password = authRequest.getPassword();
        if (!passwordService.verifyHash(password, user.HashedPassword))
            throw new AuthException("Wrong login or password");

        var accessToken = jwtProvider.generateAccessToken(user);
        var refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getLogin(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }

    public JwtResponse getAccessToken(String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken))
            throw new AuthException("Invalid token");

        var user = getUser(refreshToken);

        var accessToken = jwtProvider.generateAccessToken(user);

        return new JwtResponse(accessToken, null);
    }

    public JwtResponse refresh(String refreshToken) throws AuthException {
        if (!jwtProvider.validateRefreshToken(refreshToken))
            throw new AuthException("Invalid token");

        var user = getUser(refreshToken);

        var accessToken = jwtProvider.generateAccessToken(user);
        var newRefreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getLogin(), newRefreshToken);

        return new JwtResponse(accessToken, newRefreshToken);
    }

    private UserEntity getUser(String refreshToken) throws AuthException {
        var claims = jwtProvider.getRefreshClaims(refreshToken);
        var login = claims.getSubject();
        var saveRefreshToken = refreshStorage.get(login);

        if (saveRefreshToken == null || !saveRefreshToken.equals(refreshToken))
            throw new AuthException("Invalid token");

        return userService
                .getUser(login)
                .orElseThrow(() -> new AuthException("User not found"));
    }
}
