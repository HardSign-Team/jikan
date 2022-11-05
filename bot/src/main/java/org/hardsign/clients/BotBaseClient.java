package org.hardsign.clients;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.auth.TelegramUserAuthMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.services.auth.Authorizer;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class BotBaseClient extends RpcBaseClient{
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

    public  <TResponse> JikanResponse<TResponse> send(
            String url,
            TelegramUserAuthMeta meta,
            Function<Request.Builder, Request.Builder> requestSetup,
            Class<TResponse> typeHint) {
        return super.send(
                url,
                r -> {
                    var builder = r
                            .header("Authorization", authorizer.authorizeBot())
                            .header(JIKAN_SERVICE_AUTHORIZATION, authorizer.authorizeUser(meta));
                    return requestSetup.apply(builder);
                },
                typeHint);
    }
}
