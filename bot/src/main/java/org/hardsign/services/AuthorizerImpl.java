package org.hardsign.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.hardsign.clients.RpcBaseClient;
import org.hardsign.clients.RpcClient;
import org.hardsign.models.auth.JwtTokenDto;
import org.hardsign.models.auth.TelegramUserAuthMeta;
import org.hardsign.models.auth.requests.LoginRequest;
import org.hardsign.models.settings.BotSettings;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class AuthorizerImpl implements Authorizer {
    private static final Logger LOGGER = Logger.getLogger("AuthorizerImpl");
    private final RpcClient client;
    private final Supplier<BotSettings> settingsProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nullable
    private JwtTokenDto jwtToken; // todo: (tebaikin) 05.11.2022 should refresh this token periodically

    public AuthorizerImpl(OkHttpClient client, Supplier<BotSettings> settingsProvider) {
        this.client = new RpcClient(client, "api", settingsProvider);
        this.settingsProvider = settingsProvider;
    }

    public void init() {
        jwtToken = obtainJwtToken();
    }

    @Override
    public String authorizeBot() {
        var accessToken = jwtToken == null ? "" : jwtToken.getAccessToken();
        return "Bearer " + accessToken;
    }

    @Override
    public String authorizeUser(TelegramUserAuthMeta meta) {
        var result = toJsonSafety(meta);
        return result == null ? "" : result;
    }

    @Nullable
    private JwtTokenDto obtainJwtToken() {
        var settings = settingsProvider.get();
        var url = "api/auth/login";
        var request = new LoginRequest(settings.getBotLogin(), settings.getBotPassword());
        var json = toJsonSafety(request);

        if (json == null) {
            LOGGER.severe("Cannot obtain jwt token. To json error.");
            return null;
        }

        var body = RequestBody.create(json, RpcBaseClient.JSON);
        var result = client.send(url, r -> r.post(body), JwtTokenDto.class);
        return result.getValue().orElse(null);
    }

    @Nullable
    private String toJsonSafety(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
