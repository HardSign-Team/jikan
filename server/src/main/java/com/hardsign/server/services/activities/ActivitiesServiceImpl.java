package com.hardsign.server.services.activities;

import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityEntity;
import com.hardsign.server.models.activities.ActivityPatch;
import com.hardsign.server.models.users.User;
import com.hardsign.server.repositories.ActivitiesRepository;
import com.hardsign.server.utils.Validation;
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

    public Validation<Activity> save(User user, String name) {
        return validate(new ActivityEntity(user.getId(), name))
                .map(repository::save)
                .map(mapper::map);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public Validation<Optional<Activity>> update(long id, ActivityPatch patch) {
        return validatePatch(patch)
                .map(p -> repository
                        .findById(id)
                        .map(p::apply)
                        .map(repository::save)
                        .map(mapper::map));
    }

    private Validation<ActivityEntity> validate(ActivityEntity entity) {
        return validateName(entity.getName()).map(x -> entity);
    }

    private Validation<ActivityPatch> validatePatch(ActivityPatch patch) {
        if (patch.getName() == null)
            return Validation.fail("No changes.");
        return validateName(patch.getName()).map(x -> patch);
    }

    private Validation<String> validateName(String name) {
        if (name.isBlank())
            return Validation.fail("Name is blank");

        if (repository.findActivityEntityByName(name).isPresent())
            return Validation.fail("Activity with this name exists already.");

        return Validation.success(name);
    }
}
