package com.hardsign.server.services.auth.authentication;

import com.hardsign.server.models.auth.JwtAuthentication;
import com.hardsign.server.services.auth.JwtProvider;
import io.jsonwebtoken.Claims;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserJwtAuthenticator implements Authenticator {
    private static final String AUTHORIZATION = "Authorization";
    private final JwtProvider jwtProvider;

    public UserJwtAuthenticator(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Nullable
    @Override
    public JwtAuthentication authenticate(HttpServletRequest request) {
        return getTokenFromRequest(request)
                .flatMap(jwtProvider::getAccessClaims)
                .map(this::createJwtToken)
                .orElse(null);

    }

    private Optional<String> getTokenFromRequest(HttpServletRequest request) {
        var bearer = request.getHeader(AUTHORIZATION);
        var prefix = "Bearer ";

        if (bearer == null || !bearer.startsWith(prefix))
            return Optional.empty();

        return Optional.of(bearer.substring(prefix.length()));
    }

    private JwtAuthentication createJwtToken(Claims claims) {
        return JwtAuthentication.builder()
                .firstName(claims.get("name", String.class))
                .username(claims.getSubject())
                .authenticated(true)
                .build();
    }
}
