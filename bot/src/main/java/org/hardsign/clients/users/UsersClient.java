package org.hardsign.clients.users;

import org.hardsign.models.JikanResponse;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.requests.CreateUserRequest;
import org.hardsign.models.users.requests.FindUserByLoginRequest;
import org.hardsign.models.users.requests.GetUserByIdRequest;

public interface UsersClient {
    JikanResponse<UserDto> create(CreateUserRequest request);
    JikanResponse<UserDto> findByLogin(FindUserByLoginRequest request);
    JikanResponse<UserDto> getUserById(GetUserByIdRequest request);
}
