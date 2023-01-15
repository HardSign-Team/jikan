package org.hardsign.services.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.hardsign.clients.RpcClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.auth.JwtTokenDto;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.auth.UserAuthMeta;
import org.hardsign.models.auth.requests.LoginRequest;
import org.hardsign.models.auth.requests.RefreshRequest;
import org.hardsign.models.settings.BotSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.util.Optional;
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

    @Override
    public String authorizeBot() {
        var accessToken = getTokens().map(JwtTokenDto::getAccessToken).orElse("");
        return "Bearer " + accessToken;
    }

    @Override
    public String authorizeUser(TelegramUserMeta meta) {
        var auth = new UserAuthMeta(Long.toString(meta.getId()), meta.getLogin());
        return toJsonSafety(auth).map(AuthorizerImpl::toBase64).orElse("");
    }

    public void init() {
        var settings = settingsProvider.get();
        var period = settings.getAccessTokenLifeTime().toMinutes();
        scheduler.scheduleAtFixedRate(this::updateToken, 0, period, TimeUnit.MINUTES);
    }

    private void updateToken() {
        try {
            jwtToken = getTokens()
                    .map(this::requestRefresh)
                    .orElseGet(this::requestLogin)
                    .getValueOrThrow();
            LOGGER.info("Successfully update jwt token.");
        } catch (Exception e) {
            LOGGER.warning("Error occurred during updating jwt token. Error: " + e.getMessage());
        }
    }

    private JikanResponse<JwtTokenDto> requestRefresh(JwtTokenDto dto) {
        var request = new RefreshRequest(dto.getRefreshToken());
        return client.post("auth/refresh", request, this::addAuthorization, JwtTokenDto.class);
    }

    @NotNull
    private Request.Builder addAuthorization(Request.Builder r) {
        return r.header(client.AUTHORIZATION_HEADER, authorizeBot());
    }

    private JikanResponse<JwtTokenDto> requestLogin() {
        var settings = settingsProvider.get();
        var request = new LoginRequest(settings.getBotLogin(), settings.getBotPassword());
        return client.post("auth/login", request, JwtTokenDto.class);
    }

    private Optional<JwtTokenDto> getTokens() {
        return Optional.ofNullable(jwtToken);
    }

    private static String toBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    private Optional<String> toJsonSafety(Object obj) {
        try {
            return Optional.of(objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            LOGGER.severe(e.getMessage());
            return Optional.empty();
        }
    }
}
