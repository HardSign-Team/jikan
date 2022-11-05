package org.hardsign.clients.users;

import okhttp3.OkHttpClient;
import org.hardsign.clients.BotBaseClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.requests.CreateUserRequest;
import org.hardsign.services.auth.Authorizer;

import java.util.function.Supplier;

public class UsersClientImpl extends BotBaseClient implements UsersClient {
    public UsersClientImpl(OkHttpClient client, Authorizer authorizer, Supplier<BotSettings> settingsProvider) {
        super(client, "users", authorizer, settingsProvider);
    }

    @Override
    public JikanResponse<UserDto> create(BotRequest<CreateUserRequest> request) {
        return post("", request, UserDto.class);
    }
}
