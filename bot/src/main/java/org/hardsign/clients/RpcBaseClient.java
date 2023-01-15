package org.hardsign.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.*;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.settings.BotSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class RpcBaseClient {
    public final String AUTHORIZATION_HEADER = "Authorization";
    public static final MediaType JSON = MediaType.get("application/json");
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
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

    public <TRequest, TResponse> JikanResponse<TResponse> post(String url, TRequest request, Class<TResponse> typeHint) {
        return post(url, request, r -> r, typeHint);
    }

    public <TRequest, TResponse> JikanResponse<TResponse> post(
            String url,
            TRequest request,
            Function<Request.Builder, Request.Builder> requestSetup,
            Class<TResponse> typeHint) {
        String json;
        try {
            json = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return send(url, requestSetup.andThen(r -> r.post(RequestBody.create(json, JSON))), typeHint);
    }

    public <TResponse> JikanResponse<TResponse> send(
            String url,
            Function<Request.Builder, Request.Builder> requestSetup,
            Class<TResponse> typeHint) {
        var endpoint = getEndpoint(url);
        System.out.println(endpoint);

        var request = requestSetup.andThen(addEndpoint(endpoint)).apply(new Request.Builder()).build();
        try {
            try (var response = client.newCall(request).execute()) {
                return toJikanResponse(response, typeHint);
            }
        } catch (Exception e) {
            return new JikanResponse<>(418, e.toString());
        }
    }

    @NotNull
    private static Function<Request.Builder, Request.Builder> addEndpoint(String endpoint) {
        return x -> x.url(endpoint);
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

    protected JikanResponse<String> toJsonSafety(@Nullable Object obj) {
        try {
            return new JikanResponse<>(objectMapper.writeValueAsString(obj), 200);
        } catch (JsonProcessingException e) {
            return new JikanResponse<>(400, e.toString());
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
            return new JikanResponse<>(response.code(), e.getMessage());
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

