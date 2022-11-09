package com.hardsign.server.services.auth;

import com.hardsign.server.models.auth.JwtAuthentication;
import com.hardsign.server.models.users.User;
import com.hardsign.server.services.auth.authentication.ServiceJwtAuthenticator;
import com.hardsign.server.services.auth.authentication.UserJwtAuthenticator;
import com.hardsign.server.services.user.UserService;
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
    private final UserJwtAuthenticator userAuthenticator;
    private final ServiceJwtAuthenticator serviceJwtAuthenticator;
    private final UserService userService;

    public JwtFilter(
            UserJwtAuthenticator userAuthenticator,
            ServiceJwtAuthenticator serviceJwtAuthenticator,
            UserService userService) {
        this.userAuthenticator = userAuthenticator;
        this.serviceJwtAuthenticator = serviceJwtAuthenticator;
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        var authentication = authenticate((HttpServletRequest) request);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    @Nullable
    private JwtAuthentication authenticate(HttpServletRequest servletRequest) {
        var userAuthentication = userAuthenticator.authenticate(servletRequest);
        return userAuthentication != null && isService((String) userAuthentication.getPrincipal())
                ? serviceJwtAuthenticator.authenticate(servletRequest)
                : userAuthentication;

    }

    private boolean isService(String login) {
        return userService.getUserByLogin(login)
                .map(User::isService)
                .orElse(false);
    }
}
