package com.hardsign.server.services.auth;

import com.hardsign.server.models.auth.JwtTokens;
import com.hardsign.server.models.auth.RefreshTokenEntity;
import com.hardsign.server.models.users.User;
import com.hardsign.server.repositories.RefreshTokensRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtProvider jwtProvider;
    private final PasswordService passwordService;
    private final RefreshTokensRepository refreshTokensRepository;

    public AuthServiceImpl(JwtProvider jwtProvider,
                           PasswordService passwordService,
                           RefreshTokensRepository refreshTokensRepository) {
        this.jwtProvider = jwtProvider;
        this.passwordService = passwordService;
        this.refreshTokensRepository = refreshTokensRepository;
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

        var newToken = new RefreshTokenEntity();
        newToken.setUserLogin(user.getLogin());
        newToken.setActive(true);
        newToken.setRefreshToken(newRefreshToken);

        var activeToken = refreshTokensRepository.findRefreshTokenEntityByUserLoginAndActive(user.getLogin(), true);

        if (activeToken.isPresent()){
            activeToken.get().setActive(false);
            refreshTokensRepository.saveAll(List.of(activeToken.get(), newToken));
        }
        else {
            refreshTokensRepository.save(newToken);
        }

        return new JwtTokens(accessToken, newRefreshToken);
    }

    @Override
    public boolean verifyToken(String login, String refreshToken) {
        var token = refreshTokensRepository.findRefreshTokenEntityByUserLoginAndActive(login, true);

        if (token.isPresent() && Objects.equals(token.get().refreshToken, refreshToken))
            return true;

        var expiredToken = refreshTokensRepository.findRefreshTokenEntityByUserLoginAndActiveAndRefreshToken(login, false, refreshToken);

        // this means that we got request with old refreshToken and possible that token was cracked
        if (expiredToken.isPresent() && token.isPresent()){
            token.get().setActive(false);
            refreshTokensRepository.save(token.get());
        }

        return false;
    }
}
