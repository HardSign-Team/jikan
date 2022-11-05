package com.hardsign.server.services.auth.authentication;

import com.hardsign.server.models.auth.JwtAuthentication;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

public interface Authenticator {
    @Nullable
    JwtAuthentication authenticate(HttpServletRequest request);
}
