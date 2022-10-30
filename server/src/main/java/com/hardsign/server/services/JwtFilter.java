package com.hardsign.server.services;

import com.hardsign.server.models.auth.JwtAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        var token = getTokenFromRequest((HttpServletRequest) request);

        if (token == null || !jwtProvider.validateAccessToken(token)){
            fc.doFilter(request, response);
            return;
        }

        var claims = jwtProvider.getAccessClaims(token);

        var jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setFirstName(claims.get("name", String.class));
        jwtInfoToken.setUsername(claims.getSubject());

        jwtInfoToken.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        var bearer = request.getHeader(AUTHORIZATION);

        if (!StringUtils.hasText(bearer) || !bearer.startsWith("Bearer "))
            return null;

        return bearer.substring(7);
    }
}
