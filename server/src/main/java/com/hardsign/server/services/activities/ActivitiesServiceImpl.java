package com.hardsign.server.services.activities;

import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityEntity;
import com.hardsign.server.models.activities.ActivityPatch;
import com.hardsign.server.repositories.ActivitiesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ActivitiesServiceImpl implements ActivitiesService {
    private final ActivitiesRepository repository;
    private final Mapper mapper;

    public ActivitiesServiceImpl(ActivitiesRepository repository, Mapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Activity> findAllActivities() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    public Optional<Activity> findById(long id) {
        return repository.findById(id).map(mapper::map);
    }

    public Activity insert(long userId, String name) {
        return mapper.map(repository.save(new ActivityEntity(0, userId, name)));
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
