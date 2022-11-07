package org.hardsign.repositories;

import org.hardsign.models.users.UserState;

import java.util.Optional;

public interface UserStateRepository {
    Optional<UserState> findByUserId(long userId);
    UserState save(UserState entity);
}

