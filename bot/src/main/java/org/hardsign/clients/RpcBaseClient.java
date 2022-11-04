package org.hardsign.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.hardsign.models.JikanResponse;

import java.io.IOException;
import java.util.Objects;

public abstract class RpcBaseClient {
    public static final MediaType JSON = MediaType.get("application/json");
    private final OkHttpClient client;
    private final String baseUrl;

    protected RpcBaseClient(OkHttpClient client, String baseUrl) {
        this.client = client;
        this.baseUrl = "http://localhost:8080/api/" + baseUrl;
    }

    protected <T> JikanResponse<T> post(String url, String json, Class<T> typeHint) {
        var body = RequestBody.create(json, JSON);
        var endpoint = getEndpoint(url);
        var request = new Request.Builder()
                .url(endpoint)
                .post(body)
                .build();
        try {
            try (var response = client.newCall(request).execute()) {
                return toJikanResponse(response, typeHint);
            }
        } catch (Exception e) {
            return new JikanResponse<>(500, e.getMessage());
        }
    }

    protected <T> JikanResponse<T> get(String url, Class<T> typeHint) {
        var endpoint = getEndpoint(url);
        var request = new Request.Builder()
                .url(endpoint)
                .get()
                .header("Authorization", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdHJpbmciLCJleHAiOjE2Njc1ODM3MTgsIm5hbWUiOiJzdHJpbmcifQ.rnWJTdjBMJlsnLAx9apKucHl-i5Tc1GhAHjDjP2ml6H7GFQG5B4BYvqUkT3R5jIHU4wsA30YRZzpbkN1tep9bQ")
                .build();
        try {
            try (var response = client.newCall(request).execute()) {
                return toJikanResponse(response, typeHint);
            }
        } catch (Exception e) {
            return new JikanResponse<>(500, e.getMessage());
        }
    }

    private <T> JikanResponse<T> toJikanResponse(Response response, Class<T> type) throws IOException {
        var responseBody = response.body();
        return responseBody != null
                ? new JikanResponse<T>(new ObjectMapper().readValue(responseBody.bytes(), type), response.code())
                : new JikanResponse<T>(response.code(), response.message());
    }

    private String getEndpoint(String url) {
        return Objects.equals(url, "") ? baseUrl : String.format("%s/%s", baseUrl, url);
    }
}
