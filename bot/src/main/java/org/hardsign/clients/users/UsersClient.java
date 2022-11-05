package org.hardsign.clients.users;

import org.hardsign.models.JikanResponse;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.requests.CreateUserRequest;

public interface UsersClient {
    JikanResponse<UserDto> create(BotRequest<CreateUserRequest> request);
}
