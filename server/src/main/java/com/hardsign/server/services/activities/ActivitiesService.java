package com.hardsign.server.services.activities;

import com.hardsign.server.exceptions.DomainException;
import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityPatch;
import com.hardsign.server.models.users.User;

import java.util.List;
import java.util.Optional;

public interface ActivitiesService {
    List<Activity> findAllActivitiesByUser(User user);

    Optional<Activity> findById(long id);

    Activity save(User user, String name) throws DomainException;

    void delete(long id);

    Optional<Activity> update(long id, ActivityPatch patch) throws DomainException;
}
