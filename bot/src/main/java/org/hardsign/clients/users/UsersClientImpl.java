package org.hardsign.clients.users;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.hardsign.clients.BotBaseClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.requests.CreateUserRequest;
import org.hardsign.models.users.requests.FindUserByLoginRequest;
import org.hardsign.services.auth.Authorizer;

import java.util.function.Supplier;

public class UsersClientImpl extends BotBaseClient implements UsersClient {
    public UsersClientImpl(OkHttpClient client, Authorizer authorizer, Supplier<BotSettings> settingsProvider) {
        super(client, "api/users", authorizer, settingsProvider);
    }

    @Override
    public JikanResponse<UserDto> create(BotRequest<CreateUserRequest> request) {
        var payload = request.getRequest();
        var meta = request.getUserMeta();
        return sendBody(payload, json -> send(
                "", meta,
                r -> r
                        .removeHeader(BotBaseClient.JIKAN_SERVICE_AUTHORIZATION)
                        .post(RequestBody.create(json, JSON)),
                UserDto.class));

    }

    @Override
    public JikanResponse<UserDto> findByLogin(BotRequest<FindUserByLoginRequest> request) {
        var login = request.getRequest().getLogin();
        return send(
                "login/" + login,
                request.getUserMeta(),
                r -> r
                        .removeHeader(BotBaseClient.JIKAN_SERVICE_AUTHORIZATION)
                        .get(),
                UserDto.class);
    }

}
