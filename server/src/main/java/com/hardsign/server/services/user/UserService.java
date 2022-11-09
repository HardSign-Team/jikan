package com.hardsign.server.services.user;

import com.hardsign.server.models.users.User;
import com.hardsign.server.models.users.UserEntity;

import java.util.Optional;

public interface UserService {
    Optional<UserEntity> getUserByLogin(String login);

    Optional<UserEntity> findById(long id);

    User save(UserEntity user);
}
