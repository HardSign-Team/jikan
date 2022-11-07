package com.hardsign.server.services.activities;

import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityPatch;
import com.hardsign.server.models.users.User;
import com.hardsign.server.utils.Validation;

import java.util.List;
import java.util.Optional;

public interface ActivitiesService {
    List<Activity> findAllActivitiesByUser(User user);

    Optional<Activity> findById(long id);

    Validation<Activity> save(User user, String name);

    void delete(long id);

    Validation<Optional<Activity>> update(long id, ActivityPatch patch);
}
