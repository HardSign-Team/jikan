package com.hardsign.server.controllers;


import com.hardsign.server.models.activities.ActivityEntity;
import com.hardsign.server.models.activities.ActivityModel;
import com.hardsign.server.services.Activity.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/activity")
public class ActivitiesController {
    private final ActivityService activityService;

    public ActivitiesController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping()
    public List<ActivityModel> getAllActivities() {
        System.out.println("PRONT");
        return activityService
                .findAllActivities()
                .stream()
                .map(entity -> new ActivityModel(entity.UserId.toString(), entity.Id.toString(), entity.Name))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityModel> getActivityById(@PathVariable("id") String id) {
        var uuid = getUUIDfromString(id);
        var activity = Optional.of(activityService.findById(uuid));
        return activity
                .map(x -> new ActivityModel(x.UserId.toString(), x.Id.toString(), x.Name))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public ResponseEntity<String> create(@RequestBody ActivityModel activityModel) {
        if (activityModel.Id == null) {
            return ResponseEntity.badRequest().body("Request does not contain a body");
        }

        if (activityService.insert(getActivityEntityByActivityModel(activityModel))) {
            return ResponseEntity.ok("Activity is created");
        }

        return ResponseEntity.badRequest().body("Can't create");
    }

    @GetMapping(value = "/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable String id) {
        var uuid = getUUIDfromString(id);
        if (id == null) {
            return ResponseEntity.badRequest().body("Request does not contain a body");
        }
        if (activityService.delete(uuid)) {
            return ResponseEntity.ok("Activity is deleted");
        }
        return ResponseEntity.badRequest().body( "Cannot delete the activity");
    }

    @GetMapping("/update")
    @ResponseBody
    public ResponseEntity<String> update(@RequestBody ActivityModel activityModel) {
        if (activityModel.Id == null) {
            return ResponseEntity.badRequest().body("Request does not contain id");
        }

        if (activityService.update(getActivityEntityByActivityModel(activityModel))) {
            return ResponseEntity.ok("Activity is updated");
        }
        return ResponseEntity.badRequest().body( "Cannot update the activity");
    }

    private UUID getUUIDfromString(String str) {
        try {
            return UUID.fromString(str);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private ActivityEntity getActivityEntityByActivityModel(ActivityModel activityModel) {
        var activityEntity = new ActivityEntity();
        activityEntity.Name = activityModel.Name;
        activityEntity.UserId = getUUIDfromString(activityModel.UserId);
        activityEntity.Id = getUUIDfromString(activityModel.Id);
        return activityEntity;
    }
}