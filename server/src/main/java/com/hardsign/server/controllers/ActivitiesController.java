package com.hardsign.server.controllers;


import com.hardsign.server.exceptions.BadRequestException;
import com.hardsign.server.exceptions.ForbiddenException;
import com.hardsign.server.exceptions.NotFoundException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.ActivityModel;
import com.hardsign.server.models.activities.ActivityPatch;
import com.hardsign.server.models.activities.requests.CreateActivityRequest;
import com.hardsign.server.models.activities.requests.PatchActivityRequest;
import com.hardsign.server.models.users.User;
import com.hardsign.server.services.activities.ActivitiesService;
import com.hardsign.server.services.user.CurrentUserProvider;
import com.hardsign.server.utils.users.UserUtils;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ActivityModel create(@Valid @RequestBody CreateActivityRequest request) {
        var user = getUserOrThrow();

        var activity = activityService.save(user, request.getName())
                .orElseThrow(BadRequestException::new);

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

        return activityService.update(request.getId(), patch)
                .orElseThrow(BadRequestException::new)
                .map(mapper::mapToModel)
                .orElseThrow(NotFoundException::new);
    }

    private User getUserOrThrow() {
        return UserUtils.getUserOrThrow(currentUserProvider);
    }
}