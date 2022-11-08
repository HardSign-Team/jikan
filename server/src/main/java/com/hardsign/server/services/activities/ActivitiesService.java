package com.hardsign.server.services.activities;

import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.users.User;
import com.hardsign.server.utils.Validation;

import java.util.List;
import java.util.Optional;

public interface ActivitiesService {
    List<Activity> findAllActivitiesByUser(User user);

    Optional<Activity> findById(long id);

    Activity save(Activity activity);

    void delete(long id);

    Validation<Activity> validate(Activity activity);
}
