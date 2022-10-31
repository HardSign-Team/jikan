package com.hardsign.server.Services.Activity;

import com.hardsign.server.models.activities.ActivityEntity;

import java.util.List;
import java.util.UUID;

public interface IActivityService {
    List<ActivityEntity> findAllActivities();

    ActivityEntity findById(UUID id);

    ActivityEntity insert(ActivityEntity p);

    boolean delete(UUID id);

    boolean update(ActivityEntity p);
}
