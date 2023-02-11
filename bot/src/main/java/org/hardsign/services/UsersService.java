package org.hardsign.services;

import org.hardsign.clients.JikanApiClient;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.requests.CreateUserRequest;

public class UsersService {
    private final JikanApiClient jikanApiClient;

    public UsersService(JikanApiClient jikanApiClient) {
        this.jikanApiClient = jikanApiClient;
    }
    public UserDto registerUser(CreateUserRequest request) throws Exception {
        return jikanApiClient.users().create(request).getValueOrThrow();
    }
}
