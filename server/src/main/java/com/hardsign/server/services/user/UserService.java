package com.hardsign.server.services.user;

import com.hardsign.server.models.users.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserByLogin(String login);

    Optional<User> findById(long id);

    User create(String name, String login, String password);
}
