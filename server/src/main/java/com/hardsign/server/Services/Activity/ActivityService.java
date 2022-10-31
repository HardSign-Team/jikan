package com.hardsign.server.Services.Activity;

import com.hardsign.server.models.activities.ActivityEntity;
import com.hardsign.server.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ActivityService implements IActivityService {
    @Autowired
    ActivityRepository repository;

    @Override
    public List<ActivityEntity> findAllActivities() {
        return (List<ActivityEntity>)repository.findAll();
    }

    @Override
    public ActivityEntity findById(UUID id) {
        Optional<ActivityEntity> result = repository.findById(id);
        return result.orElse(null);
    }

    @Override
    public ActivityEntity insert(ActivityEntity p) {
        return repository.save(p);
    }

    @Override
    public boolean delete(UUID id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(ActivityEntity p) {
        try {
            repository.save(p);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
