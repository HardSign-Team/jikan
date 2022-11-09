package com.hardsign.server.controllers;


import com.hardsign.server.exceptions.BadRequestException;
import com.hardsign.server.exceptions.ConflictException;
import com.hardsign.server.exceptions.ForbiddenException;
import com.hardsign.server.exceptions.NotFoundException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityModel;
import com.hardsign.server.models.activities.ActivityPatch;
import com.hardsign.server.models.activities.requests.CreateActivityRequest;
import com.hardsign.server.models.activities.requests.PatchActivityRequest;
import com.hardsign.server.models.users.User;
import com.hardsign.server.services.activities.ActivitiesService;
import com.hardsign.server.services.user.CurrentUserProvider;
import com.hardsign.server.utils.users.UserUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/activities")
@Validated
public class ActivitiesController {
    private final ActivitiesService activityService;
    private final Mapper mapper;
    private final CurrentUserProvider currentUserProvider;

    public ActivitiesController(
            ActivitiesService activityService,
            Mapper mapper,
            CurrentUserProvider currentUserProvider) {
        this.activityService = activityService;
        this.mapper = mapper;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping()
    public List<ActivityModel> getAllActivities() {
        var user = getUserOrThrow();

        return activityService
                .findAllActivitiesByUser(user)
                .stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ActivityModel getActivityById(@Valid @Min(1) @PathVariable long id) {
        var user = getUserOrThrow();

        return activityService.findById(id)
                .filter(user::hasAccess)
                .map(mapper::mapToModel)
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping(value = "create")
    public ActivityModel create(@Valid @RequestBody CreateActivityRequest request) {
        var user = getUserOrThrow();

        var validated = activityService.validate(new Activity(0, user.getId(), request.getName()))
                .orElseThrow(ConflictException::new);

        var activity = activityService.save(validated);

        return mapper.mapToModel(activity);
    }

    @DeleteMapping(value = "{id}")
    public void delete(@Valid @Min(1) @PathVariable long id) {
        var user = getUserOrThrow();

        var activity = activityService.findById(id)
                .filter(user::hasAccess)
                .orElseThrow(NotFoundException::new);

        activityService.delete(activity.getId());
    }

    @PatchMapping()
    public ActivityModel update(@Valid @RequestBody PatchActivityRequest request) {
        var user = getUserOrThrow();
        var patch = new ActivityPatch(request.getName());

        var activity = activityService.findById(request.getId())
                .orElseThrow(NotFoundException::new);

        if (!user.hasAccess(activity))
            throw new ForbiddenException("Has not access to activity.");

        if (patch.getName() == null)
            throw new BadRequestException("No changes.");

        var patched = activityService.validate(patch.apply(activity))
                .orElseThrow(ConflictException::new);

        return mapper.mapToModel(activityService.save(patched));
    }

    private User getUserOrThrow() {
        return UserUtils.getUserOrThrow(currentUserProvider);
    }
}