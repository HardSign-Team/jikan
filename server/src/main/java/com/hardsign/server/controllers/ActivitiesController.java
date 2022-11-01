package com.hardsign.server.controllers;


import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.ActivityModel;
import com.hardsign.server.models.activities.ActivityPatch;
import com.hardsign.server.models.activities.requests.CreateActivityRequest;
import com.hardsign.server.models.activities.requests.PatchActivityRequest;
import com.hardsign.server.services.activities.ActivitiesServiceImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/activities/")
public class ActivitiesController {
    private final ActivitiesServiceImpl activityService;
    private final Mapper mapper;

    public ActivitiesController(ActivitiesServiceImpl activityService, Mapper mapper) {
        this.activityService = activityService;
        this.mapper = mapper;
    }

    @GetMapping()
    public List<ActivityModel> getAllActivities() {
        return activityService
                .findAllActivities()
                .stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<ActivityModel> getActivityById(@PathVariable("id") long id) {
        return activityService.findById(id)
                .map(mapper::mapToModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "create")
    @ResponseBody
    public ResponseEntity<Object> create(@RequestBody CreateActivityRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("Request does not contain body.");
        }

        if (request.getName() == null) {
            return ResponseEntity.badRequest().body("Request does not contain name.");
        }

        long userId = 0; // TODO: 01.11.2022 get user id
        var activity = activityService.insert(userId, request.getName());

        return ResponseEntity.ok(mapper.mapToModel(activity));
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