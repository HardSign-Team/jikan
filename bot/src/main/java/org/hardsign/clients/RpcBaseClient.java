package org.hardsign.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.services.Authorizer;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class RpcBaseClient {
    public static final MediaType JSON = MediaType.get("application/json");
    private final OkHttpClient client;
    private final Authorizer authorizer;
    private final Supplier<String> baseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected RpcBaseClient(
            OkHttpClient client,
            String baseUrl,
            Authorizer authorizer,
            Supplier<BotSettings> settingsProvider) {
        this.client = client;
        this.authorizer = authorizer;
        this.baseUrl = () -> settingsProvider.get().getBaseUrl() + baseUrl;
    }

    protected <TResponse> JikanResponse<TResponse> post(String url, @Nullable Object obj, Class<TResponse> typeHint) {
        return sendBody(obj, json -> send(url, request -> request.post(RequestBody.create(json, JSON)), typeHint));
    }

    protected <TResponse> JikanResponse<TResponse> patch(String url, @Nullable Object obj, Class<TResponse> typeHint) {
        return sendBody(obj, json -> send(url, request -> request.patch(RequestBody.create(json, JSON)), typeHint));
    }

    protected <TResponse> JikanResponse<TResponse> delete(String url, @Nullable Object obj, Class<TResponse> typeHint) {
        return sendBody(obj, json -> send(url, request -> request.delete(RequestBody.create(json, JSON)), typeHint));
    }

    protected <T> JikanResponse<T> get(String url, Class<T> typeHint) {
        return send(url, Request.Builder::get, typeHint);
    }

    private <TResponse> JikanResponse<TResponse> sendBody(
            @Nullable Object obj,
            Function<String, JikanResponse<TResponse>> sender) {
        var jsonResponse = toJsonSafety(obj);
        return jsonResponse
                .getValue()
                .map(sender)
                .orElseGet(() -> new JikanResponse<>(jsonResponse.getCode(), jsonResponse.getError()));
    }

    private <TResponse> JikanResponse<TResponse> send(
            String url,
            Function<Request.Builder, Request.Builder> requestSetup,
            Class<TResponse> typeHint) {
        var endpoint = getEndpoint(url);
        var requestBuilder = new Request.Builder()
                .url(endpoint)
                .header("Authorization", authorizer.authorize());
        var request = requestSetup.apply(requestBuilder).build();
        try {
            try (var response = client.newCall(request).execute()) {
                return toJikanResponse(response, typeHint);
            }
        } catch (Exception e) {
            return new JikanResponse<>(500, e.getMessage());
        }
    }

    private JikanResponse<String> toJsonSafety(@Nullable Object obj) {
        try {
            return new JikanResponse<>(objectMapper.writeValueAsString(obj), 200);
        } catch (JsonProcessingException e) {
            return new JikanResponse<>(400, e.getMessage());
        }
    }

    private <T> JikanResponse<T> toJikanResponse(Response response, Class<T> type) throws IOException {
        var responseBody = response.body();
        return responseBody != null
                ? new JikanResponse<T>(objectMapper.readValue(responseBody.bytes(), type), response.code())
                : new JikanResponse<T>(response.code(), response.message());
    }

    private String getEndpoint(String url) {
        var prefixUrl = baseUrl.get();
        return Objects.equals(url, "") ? prefixUrl : String.format("%s/%s", prefixUrl, url);
    }
}
