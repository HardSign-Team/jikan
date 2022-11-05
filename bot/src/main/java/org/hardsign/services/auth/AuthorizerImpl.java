package org.hardsign.services.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.hardsign.clients.RpcBaseClient;
import org.hardsign.clients.RpcClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.auth.JwtTokenDto;
import org.hardsign.models.auth.TelegramUserAuthMeta;
import org.hardsign.models.auth.requests.LoginRequest;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.requests.CreateUserRequest;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
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

        jwtToken = requestLogin(request).getValueOrThrow().orElseThrow(() -> new Exception("JWT was null."));
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

    @Override
    public UserDto createUser(TelegramUserAuthMeta meta) throws Exception {
        var name = meta.getLogin();
        var login = Long.toString(meta.getId());
        var password = UUID.randomUUID().toString();
        var request = new CreateUserRequest(name, login, password);
        return requestCreateUser(request).getValueOrThrow().orElseThrow();
    }

    private JikanResponse<UserDto> requestCreateUser(CreateUserRequest request) throws JsonProcessingException {
        var json = toJson(request);
        var body = RequestBody.create(json, RpcBaseClient.JSON);
        return client.send("users", r -> r.post(body), UserDto.class);
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
