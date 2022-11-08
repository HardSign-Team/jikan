package com.hardsign.server.services.activities;

import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.Activity;
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

    public Activity save(Activity activity) {
        var entity = mapper.mapToEntity(activity);
        var saved = repository.save(entity);
        return mapper.map(saved);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    public Validation<Activity> validate(Activity entity) {
        if (entity.getName().isBlank())
            return Validation.invalid("Name is blank.");
        if (repository.findActivityEntityByUserIdAndName(entity.getUserId(), entity.getName()).isPresent())
            return Validation.invalid("Activity with this name exists already.");
        return Validation.valid(entity);
    }
}
