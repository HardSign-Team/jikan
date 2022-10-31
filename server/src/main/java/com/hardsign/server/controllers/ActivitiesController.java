package com.hardsign.server.controllers;


import com.hardsign.server.Services.Activity.ActivityService;
import com.hardsign.server.models.activities.ActivityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/activity")
public class ActivitiesController {
    @Autowired
    ActivityService activityService;

    @RequestMapping(method = RequestMethod.GET)
    public List<ActivityEntity> getAllActivities() {
        return activityService.findAllActivities();
    }

    @GetMapping("/{id}")
    public ActivityEntity getActivityById(@PathVariable("id") UUID id) {
        return activityService.findById(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestBody ActivityEntity activityEntity) {
        if (activityEntity != null) {
            activityService.insert(activityEntity);
            return "Added an activity";
        } else {
            return "Request does not contain a body";
        }
        // return new RedirectView("/activity");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable UUID id) {
        if (id != null) {
            if (activityService.delete(id)) {
                return "Deleted the activity.";
            } else {
                return "Cannot delete the activity.";
            }
        }
        return "The id is invalid for the activity.";
        // return new RedirectView("/activity");
    }

    @RequestMapping("/update")
    public String update(@RequestBody ActivityEntity activityEntity) {
        if (activityEntity != null) {
            activityService.update(activityEntity);
            return "Updated activity.";
        } else {
            return "Request does not contain a body";
        }
        // return new RedirectView("/activity");
    }
}