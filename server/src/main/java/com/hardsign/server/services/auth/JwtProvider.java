package com.hardsign.server.services.auth;

import com.hardsign.server.models.users.User;
import com.hardsign.server.services.time.TimeProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtProvider {
    private final SecretKey secretKey;
    private final int accessTokenLifeTimeMinutes;
    private final int refreshTokenLifeTimeMinutes;
    private final TimeProvider timeProvider;

    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.lifetime}") Integer accessTokenLifeTimeMinutes,
            @Value("${jwt.refresh.lifetime}") Integer refreshTokenLifeTimeMinutes,
            TimeProvider timeProvider){
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenLifeTimeMinutes = accessTokenLifeTimeMinutes;
        this.refreshTokenLifeTimeMinutes = refreshTokenLifeTimeMinutes;
        this.timeProvider = timeProvider;
    }

    public String generateAccessToken(User user) {
        var now = timeProvider.now();
        var accessExpirationInstant = now.plusMinutes(accessTokenLifeTimeMinutes).toInstant();
        var accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(accessExpiration)
                .signWith(secretKey)
                .claim("name", user.getName())
                .compact();
    }

    public String generateRefreshToken(User user) {
        var refreshExpirationInstant = timeProvider.now()
                .plusMinutes(refreshTokenLifeTimeMinutes)
                .toInstant();
        var refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setExpiration(refreshExpiration)
                .signWith(secretKey)
                .compact();
    }

    public Optional<Claims> getAccessClaims(String token) {
        return getClaims(token, secretKey);
    }

    public Optional<Claims> getRefreshClaims(String token) {
        return getClaims(token, secretKey);
    }

    private Optional<Claims> getClaims(String token, Key secret) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Optional.of(claims);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
