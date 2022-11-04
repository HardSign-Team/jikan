package com.hardsign.server.services.auth;

import com.hardsign.server.models.auth.JwtAuthentication;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtFilter extends GenericFilterBean {
    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var token = getTokenFromRequest((HttpServletRequest) request);

        if (!jwtProvider.validateAccessToken(token)){
            chain.doFilter(request, response);
            return;
        }

        var claims = jwtProvider.getAccessClaims(token);

        var jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setFirstName(claims.get("name", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        jwtInfoToken.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);

        chain.doFilter(request, response);
    }

    @Nullable
    private String getTokenFromRequest(HttpServletRequest request) {
        var bearer = request.getHeader(AUTHORIZATION);
        var prefix = "Bearer ";

        if (bearer == null || !bearer.startsWith(prefix))
            return null;

        return bearer.substring(prefix.length());
    }
}
