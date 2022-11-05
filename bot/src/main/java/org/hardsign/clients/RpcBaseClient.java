package org.hardsign.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.settings.BotSettings;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class RpcBaseClient {
    public static final MediaType JSON = MediaType.get("application/json");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client;
    private final Supplier<String> baseUrl;

    protected RpcBaseClient(
            OkHttpClient client,
            String baseUrl,
            Supplier<BotSettings> settingsProvider) {
        this.client = client;
        this.baseUrl = () -> settingsProvider.get().getBaseUrl() + baseUrl;
    }

    protected <TResponse> JikanResponse<TResponse> sendBody(
            @Nullable Object obj,
            Function<String, JikanResponse<TResponse>> sender) {
        var jsonResponse = toJsonSafety(obj);
        return jsonResponse
                .getValue()
                .map(sender)
                .orElseGet(() -> new JikanResponse<>(jsonResponse.getCode(), jsonResponse.getError()));
    }

    public <TResponse> JikanResponse<TResponse> send(
            String url,
            Function<Request.Builder, Request.Builder> requestSetup,
            Class<TResponse> typeHint) {
        var endpoint = getEndpoint(url);
        var request = requestSetup.apply(new Request.Builder().url(endpoint)).build();
        try {
            try (var response = client.newCall(request).execute()) {
                return toJikanResponse(response, typeHint);
            }
        } catch (Exception e) {
            return new JikanResponse<>(500, e.getMessage());
        }
    }

    protected JikanResponse<String> toJsonSafety(@Nullable Object obj) {
        try {
            return new JikanResponse<>(objectMapper.writeValueAsString(obj), 200);
        } catch (JsonProcessingException e) {
            return new JikanResponse<>(400, e.getMessage());
        }
    }

    private <T> JikanResponse<T> toJikanResponse(Response response, Class<T> type) throws IOException {
        var responseBody = response.body();
        return responseBody != null
                ? new JikanResponse<>(objectMapper.readValue(responseBody.bytes(), type), response.code())
                : new JikanResponse<>(response.code(), response.message());
    }

    private String getEndpoint(String url) {
        var prefixUrl = baseUrl.get();
        return Objects.equals(url, "") ? prefixUrl : String.format("%s/%s", prefixUrl, url);
    }
}

