package com.hardsign.server.services.activities;

import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityPatch;

import java.util.List;
import java.util.Optional;

public interface ActivitiesService {
    List<Activity> findAllActivities();

    Optional<Activity> findById(long id);

    Activity insert(long userId, String name);

    void delete(long id);

    Optional<Activity> update(long id, ActivityPatch patch);
}
