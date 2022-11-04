package com.hardsign.server.services.auth;

import com.hardsign.server.models.users.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtProvider {
    private final SecretKey secretKey;
    private final int accessTokenLifeTime;
    private final int refreshTokenLifeTime;

    //TODO (lunev.d): add datetime provider
    // TODO: 01.11.2022 +
    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.lifetime}") Integer accessTokenLifeTime,
            @Value("${jwt.refresh.lifetime}") Integer refreshTokenLifeTime
    ){
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenLifeTime = accessTokenLifeTime;
        this.refreshTokenLifeTime = refreshTokenLifeTime;
    }

    public String generateAccessToken(UserEntity user) {
        var now = LocalDateTime.now();
        var accessExpirationInstant = now.plusMinutes(accessTokenLifeTime).atZone(ZoneId.systemDefault()).toInstant();
        var accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(secretKey)
                .claim("name", user.getName())
                .compact();
    }

    public String generateRefreshToken(UserEntity user) {
        var now = LocalDateTime.now();
        var refreshExpirationInstant = now.plusDays(refreshTokenLifeTime).atZone(ZoneId.systemDefault()).toInstant();
        var refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(refreshExpiration)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateAccessToken(@Nullable String accessToken) {
        return validateToken(accessToken, secretKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, secretKey);
    }

    private boolean validateToken(@Nullable String token, Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, secretKey);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, secretKey);
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
