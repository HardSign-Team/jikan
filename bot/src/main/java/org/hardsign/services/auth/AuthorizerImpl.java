package org.hardsign.services.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.hardsign.clients.RpcBaseClient;
import org.hardsign.clients.RpcClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.auth.JwtTokenDto;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.auth.requests.LoginRequest;
import org.hardsign.models.settings.BotSettings;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.util.function.Supplier;

public class AuthorizerImpl implements Authorizer {
    private final RpcClient client;
    private final Supplier<BotSettings> settingsProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nullable
    private JwtTokenDto jwtToken; // todo: (tebaikin) 05.11.2022 should refresh this token periodically

    public AuthorizerImpl(OkHttpClient client, Supplier<BotSettings> settingsProvider) {
        this.client = new RpcClient(client, "api", settingsProvider);
        this.settingsProvider = settingsProvider;
    }

    public void init() throws Exception {
        var settings = settingsProvider.get();
        var request = new LoginRequest(settings.getBotLogin(), settings.getBotPassword());

        jwtToken = requestLogin(request).getValueOrThrow();
    }

    @Override
    public String authorizeBot() {
        var accessToken = jwtToken == null ? "" : jwtToken.getAccessToken();
        return "Bearer " + accessToken;
    }

    @Override
    public String authorizeUser(TelegramUserMeta meta) {
        var result = toJsonSafety(meta);
        return result == null ? "" : Base64.getEncoder().encodeToString(result.getBytes());
    }

    private JikanResponse<JwtTokenDto> requestLogin(LoginRequest request) throws Exception {
        var json = toJsonSafety(request);
        if (json == null) {
            throw new Exception("Cannot obtain jwt token. To json error.");
        }
        var body = RequestBody.create(json, RpcBaseClient.JSON);
        return client.send("auth/login", r -> r.post(body), JwtTokenDto.class);
    }

    @Nullable
    private String toJsonSafety(Object obj) {
        try {
            return toJson(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
