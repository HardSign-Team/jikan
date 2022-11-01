package com.hardsign.server.services.Activity;

import com.hardsign.server.models.activities.ActivityEntity;
import com.hardsign.server.repositories.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService implements IActivityService {
    final ActivityRepository repository;

    public ActivityService(ActivityRepository repository) {
        this.repository = repository;
    }

    public List<ActivityEntity> findAllActivities() {
        return (List<ActivityEntity>)repository.findAll();
    }

    public ActivityEntity findById(UUID id) {
        try {
            return repository.findById(id).orElse(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean insert(ActivityEntity activityEntity) {
        try {
            repository.save(activityEntity);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean delete(UUID id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean update(ActivityEntity activityEntity) {
        try {
            repository.save(activityEntity);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
