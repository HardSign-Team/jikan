package org.hardsign.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    private final String baseUrl;
    private final Supplier<BotSettings> settingsProvider;

    protected RpcBaseClient(
            OkHttpClient client,
            String baseUrl,
            Supplier<BotSettings> settingsProvider) {
        this.client = client;
        this.baseUrl = baseUrl.replaceAll("^/", "");
        this.settingsProvider = settingsProvider;
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
            return new JikanResponse<>(418, e.getMessage());
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
        if (responseBody == null) {
            return new JikanResponse<>(response.code(), response.message());
        }
        var responseBodyBytes = responseBody.bytes();
        if (responseBodyBytes.length == 0 && type == Object.class) {
            return new JikanResponse<>(null, response.code());
        }
        try {
            return new JikanResponse<>(objectMapper.readValue(responseBodyBytes, type), response.code());
        } catch (JsonMappingException e) {
            return new JikanResponse<>(response.code(), response.message());
        }
    }

    private String getEndpoint(String url) {
        var prefixUrl = getBaseUrl();
        return Objects.equals(url, "") ? prefixUrl : String.format("%s/%s", prefixUrl, url);
    }

    private String getBaseUrl() {
        var apiUrl = settingsProvider.get().getApiUrl().replaceAll("/$", "");

        return apiUrl + "/" + baseUrl;
    }
}

