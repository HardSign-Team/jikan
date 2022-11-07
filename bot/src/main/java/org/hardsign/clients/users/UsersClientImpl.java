package org.hardsign.clients.users;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.hardsign.clients.RpcBaseClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.requests.CreateUserRequest;
import org.hardsign.models.users.requests.FindUserByLoginRequest;
import org.hardsign.models.users.requests.GetUserByIdRequest;

import java.util.function.Supplier;

public class UsersClientImpl extends RpcBaseClient implements UsersClient {
    public UsersClientImpl(OkHttpClient client, Supplier<BotSettings> settingsProvider) {
        super(client, "api/users", settingsProvider);
    }

    @Override
    public JikanResponse<UserDto> create(CreateUserRequest request) {
        return sendBody(request, json -> send("", r -> r.post(RequestBody.create(json, JSON)), UserDto.class));

    }

    @Override
    public JikanResponse<UserDto> findByLogin(FindUserByLoginRequest request) {
        return send("login/" + request.getLogin(), Request.Builder::get, UserDto.class);
    }

    @Override
    public JikanResponse<UserDto> getUserById(GetUserByIdRequest request) {
        return send(Long.toString(request.getUserId()), Request.Builder::get, UserDto.class);
    }

}
