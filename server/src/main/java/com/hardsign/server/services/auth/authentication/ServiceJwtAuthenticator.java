package com.hardsign.server.services.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hardsign.server.models.auth.JwtAuthentication;
import com.hardsign.server.models.auth.UserAuthMetaModel;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Optional;

@Service
public class ServiceJwtAuthenticator implements Authenticator {
    private static final String SERVICE_AUTHORIZATION = "Jikan-Service-Authorization";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nullable
    @Override
    public JwtAuthentication authenticate(HttpServletRequest request) {
        return obtainJson(request)
                .map(this::decodeBase64)
                .flatMap(this::parseJson)
                .map(this::getAuthentication)
                .orElse(null);
    }

    private Optional<String> obtainJson(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(SERVICE_AUTHORIZATION));
    }

    private String decodeBase64(String encodedString) {
        return new String(Base64.getDecoder().decode(encodedString.getBytes()));
    }

    private Optional<UserAuthMetaModel> parseJson(String json) {
        try {
            return Optional.of(objectMapper.readValue(json, UserAuthMetaModel.class));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    private JwtAuthentication getAuthentication(UserAuthMetaModel meta) {
        return JwtAuthentication.builder()
                .firstName(meta.getName())
                .username(meta.getLogin())
                .authenticated(true)
                .build();
    }
}
