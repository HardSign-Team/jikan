package com.hardsign.server.services.activities;

import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityEntity;
import com.hardsign.server.models.activities.ActivityPatch;
import com.hardsign.server.models.users.User;
import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.repositories.ActivitiesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivitiesServiceImpl implements ActivitiesService {
    private final ActivitiesRepository repository;
    private final Mapper mapper;

    public ActivitiesServiceImpl(ActivitiesRepository repository, Mapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Activity> findAllActivitiesByUser(User user) {
        return repository.findActivityEntitiesByUserId(user.getId())
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    public Optional<Activity> findById(long id) {
        return repository.findById(id).map(mapper::map);
    }

    public Activity save(User user, String name) {
        final var entity = new ActivityEntity();
        entity.setUser(new UserEntity(user.getId()));
        entity.setName(name);

        final var savedEntity = repository.save(entity);

        return mapper.map(savedEntity);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public Optional<Activity> update(long id, ActivityPatch patch) {
        return repository
                .findById(id)
                .map(repository::save)
                .map(mapper::map);
    }
}
