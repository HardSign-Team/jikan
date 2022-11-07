package org.hardsign.repositories;

import org.hardsign.models.users.UserState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// todo: (tebaikin) 05.11.2022 should use real db
public class UserStateRepositoryImpl implements UserStateRepository {

    private final List<UserState> entities = new ArrayList<>();

    @Override
    public Optional<UserState> findByUserId(long userId) {
        return entities.stream().filter(x -> x.getUserId() == userId).findFirst();
    }

    @Override
    public UserState save(UserState entity) {
        entities.removeIf(x -> x.getUserId() == entity.getUserId());
        entities.add(entity);
        return entity;
    }
}
