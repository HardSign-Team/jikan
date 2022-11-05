package org.hardsign.repositories;

import org.hardsign.models.users.UserStateEntity;

import java.util.Optional;

public interface UserStateRepository {
    Optional<UserStateEntity> findByUserId(long userId);
    UserStateEntity save(UserStateEntity entity);
}

