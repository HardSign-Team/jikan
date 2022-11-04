package com.hardsign.server.controllers;


import com.hardsign.server.exceptions.ForbiddenException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.ActivityModel;
import com.hardsign.server.models.activities.ActivityPatch;
import com.hardsign.server.models.activities.requests.CreateActivityRequest;
import com.hardsign.server.models.activities.requests.PatchActivityRequest;
import com.hardsign.server.models.users.User;
import com.hardsign.server.services.activities.ActivitiesService;
import com.hardsign.server.services.user.CurrentUserProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<ActivityModel> getAllActivities() {
        var user = getUserOrThrow();

        return activityService
                .findAllActivitiesByUser(user)
                .stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<ActivityModel> getActivityById(@Valid @Min(1) @PathVariable("id") long id) {

        var user = getUserOrThrow();

        return activityService.findById(user, id)
                .map(mapper::mapToModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityModel> create(@Valid @RequestBody CreateActivityRequest request) {
        var user = getUserOrThrow();

        var activity = activityService.save(user, request.getName());
        var activityModel = mapper.mapToModel(activity);

        return ResponseEntity.ok(activityModel);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Object> delete(@Valid @Min(1) @PathVariable long id) {

        // TODO: 01.11.2022 validate user rights
        activityService.delete(id);

        return ResponseEntity.ok("Activity is deleted");
    }

    @PatchMapping()
    public HttpEntity<?> update(@Valid @RequestBody PatchActivityRequest request) {

        return activityService.update(request.getId(), new ActivityPatch(request.getName()))
                .map(mapper::mapToModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    private User getUserOrThrow() {
        return currentUserProvider.getCurrentUser()
                .orElseThrow(ForbiddenException::new);
    }
}