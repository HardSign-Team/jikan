package com.hardsign.server.services.activities;

import com.hardsign.server.exceptions.DomainException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityEntity;
import com.hardsign.server.models.activities.ActivityPatch;
import com.hardsign.server.models.users.User;
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
        var entity = repository.findById(id);
        return entity.map(mapper::map);
    }

    public Activity save(User user, String name) throws DomainException {
        var entity = new ActivityEntity(user.getId(), name);

        validateName(name);

        var savedEntity = repository.save(entity);

        return mapper.map(savedEntity);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public Optional<Activity> update(long id, ActivityPatch patch) throws DomainException {
        if (patch.getName() != null)
            validateName(patch.getName());
        return repository
                .findById(id)
                .map(patch::apply)
                .map(repository::save)
                .map(mapper::map);
    }

    private void validateName(String name) throws DomainException {
        if (name.isBlank())
            throw new DomainException("Name is blank");

        if (repository.findActivityEntityByName(name).isPresent())
            throw new DomainException("Activity with this name exists already.");
    }
}
