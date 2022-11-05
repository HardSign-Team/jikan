package org.hardsign.clients.users;

import org.hardsign.models.JikanResponse;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.requests.CreateUserRequest;
import org.hardsign.models.users.requests.FindUserByLoginRequest;
import org.hardsign.models.users.requests.GetUserByIdRequest;

public interface UsersClient {
    JikanResponse<UserDto> create(BotRequest<CreateUserRequest> request);
    JikanResponse<UserDto> findByLogin(BotRequest<FindUserByLoginRequest> request);
    JikanResponse<UserDto> getUserById(BotRequest<GetUserByIdRequest> request);
}
