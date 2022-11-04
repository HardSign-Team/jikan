package com.hardsign.server.controllers;


import com.hardsign.server.exceptions.BadRequestException;
import com.hardsign.server.exceptions.ForbiddenException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.ActivityModel;
import com.hardsign.server.models.activities.ActivityPatch;
import com.hardsign.server.models.activities.requests.CreateActivityRequest;
import com.hardsign.server.models.activities.requests.PatchActivityRequest;
import com.hardsign.server.services.activities.ActivitiesService;
import com.hardsign.server.services.user.CurrentUserProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/activities")
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
        var user = currentUserProvider.getCurrentUser().orElseThrow(ForbiddenException::new);

        return activityService
                .findAllActivitiesByUser(user)
                .stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<ActivityModel> getActivityById(@PathVariable("id") long id) {
        var user = currentUserProvider.getCurrentUser().orElseThrow(ForbiddenException::new); // todo: (tebaikin) 04.11.2022 extract method

        return activityService.findById(user, id)
                .map(mapper::mapToModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityModel> create(@RequestBody CreateActivityRequest request) {
        if (request == null) {
            throw new BadRequestException("Request does not contain body.");
        }

        if (request.getName() == null) {
            throw new BadRequestException("Name cannot be null.");
        }

        var user = currentUserProvider.getCurrentUser().orElseThrow(ForbiddenException::new);

        var activity = activityService.save(user, request.getName());
        var activityModel = mapper.mapToModel(activity);

        return ResponseEntity.ok(activityModel);
    }

    @DeleteMapping(value = "{id}")
    @ResponseBody
    public ResponseEntity<Object> delete(@PathVariable long id) {
        if (id == 0) {
            return ResponseEntity.badRequest().body("Request does not contain a body");
        }

        // TODO: 01.11.2022 validate user rights
        activityService.delete(id);

        return ResponseEntity.ok("Activity is deleted");
    }

    @PatchMapping()
    @ResponseBody
    public HttpEntity<?> update(@RequestBody PatchActivityRequest request) {

        if (request == null) {
            return ResponseEntity.badRequest().body("Request does not contain id");
        }

        return activityService.update(request.getId(), new ActivityPatch(request.getName()))
                .map(mapper::mapToModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}