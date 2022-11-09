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
import org.hardsign.models.auth.UserAuthMeta;
import org.hardsign.models.auth.requests.LoginRequest;
import org.hardsign.models.settings.BotSettings;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class AuthorizerImpl implements Authorizer {
    private static final Logger LOGGER = Logger.getLogger("AuthorizerImpl");
    private final RpcClient client;
    private final Supplier<BotSettings> settingsProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Nullable
    private JwtTokenDto jwtToken;

    public AuthorizerImpl(OkHttpClient client, Supplier<BotSettings> settingsProvider) {
        this.client = new RpcClient(client, "api", settingsProvider);
        this.settingsProvider = settingsProvider;
    }

    public void init() {
        var settings = settingsProvider.get();
        var period = settings.getAccessTokenLifeTime().toMinutes();
        scheduler.scheduleAtFixedRate(this::updateJwtToken, 0, period, TimeUnit.MINUTES);
    }

    private void updateJwtToken() {
        var settings = settingsProvider.get();
        var request = new LoginRequest(settings.getBotLogin(), settings.getBotPassword());
        try {
            jwtToken = requestLogin(request).getValueOrThrow();
            LOGGER.info("Successfully update jwt token.");
        } catch (Exception e) {
            LOGGER.warning("Error occurred during updating jwt token. Error: " + e.getMessage());
        }
    }

    @Override
    public String authorizeBot() {
        var accessToken = jwtToken == null ? "" : jwtToken.getAccessToken();
        return "Bearer " + accessToken;
    }

    @Override
    public String authorizeUser(TelegramUserMeta meta) {
        var auth = new UserAuthMeta(Long.toString(meta.getId()), meta.getLogin());
        var result = toJsonSafety(auth);
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
