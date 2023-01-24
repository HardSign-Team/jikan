package org.hardsign.clients;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.services.auth.Authorizer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BotBaseClient extends RpcBaseClient {
    public static final String JIKAN_SERVICE_AUTHORIZATION = "Jikan-Service-Authorization";
    private final Authorizer authorizer;


    protected BotBaseClient(
            OkHttpClient client,
            String baseUrl,
            Authorizer authorizer,
            Supplier<BotSettings> settingsProvider) {
        super(client, baseUrl, settingsProvider);
        this.authorizer = authorizer;
    }

    public <TResponse> JikanResponse<TResponse> put(String url, BotRequest<?> request, Class<TResponse> typeHint) {
        var payload = request.getRequest();
        var meta = request.getUserMeta();
        return sendBody(payload, json -> send(url, meta, r -> r.put(RequestBody.create(json, JSON)), typeHint));
    }

    public <TResponse> JikanResponse<TResponse> post(String url, BotRequest<?> request, Class<TResponse> typeHint) {
        var payload = request.getRequest();
        var meta = request.getUserMeta();
        return sendBody(payload, json -> send(url, meta, r -> r.post(RequestBody.create(json, JSON)), typeHint));
    }

    public <TResponse> JikanResponse<TResponse> patch(String url, BotRequest<?> request, Class<TResponse> typeHint) {
        var payload = request.getRequest();
        var meta = request.getUserMeta();
        return sendBody(payload, json -> send(url, meta, r -> r.patch(RequestBody.create(json, JSON)), typeHint));
    }

    public <TResponse> JikanResponse<TResponse> delete(String url, BotRequest<?> request, Class<TResponse> typeHint) {
        var payload = request.getRequest();
        var meta = request.getUserMeta();
        return sendBody(payload, json -> send(url, meta, r -> r.delete(RequestBody.create(json, JSON)), typeHint));
    }

    public <T> JikanResponse<T> get(String url, BotRequest<?> request, Class<T> typeHint) {
        return send(url, request.getUserMeta(), Request.Builder::get, typeHint);
    }

    public <TResponse> JikanResponse<TResponse> send(
            String url,
            TelegramUserMeta meta,
            Function<Request.Builder, Request.Builder> requestSetup,
            Class<TResponse> typeHint) {
        var botAuthorization = authorizer.authorizeBot();
        var userAuthorization = authorizer.authorizeUser(meta);
        return super.send(url, addAuthorization(botAuthorization, userAuthorization).andThen(requestSetup), typeHint);
    }

    @NotNull
    private Function<Request.Builder, Request.Builder> addAuthorization(
            String botAuthorization,
            String userAuthorization) {
        return (Request.Builder r) -> r
                .header(AUTHORIZATION_HEADER, botAuthorization)
                .header(JIKAN_SERVICE_AUTHORIZATION, userAuthorization);
    }
}
