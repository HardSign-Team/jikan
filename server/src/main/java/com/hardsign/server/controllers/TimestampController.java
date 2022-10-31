package com.hardsign.server.controllers;

import com.hardsign.server.Services.Timestamp.TimestampService;
import com.hardsign.server.models.timestamps.TimestampEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/timestamp")
public class TimestampController {
    @Autowired
    TimestampService timestampService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TimestampEntity> getAllTimestamps() {
        return timestampService.findAllActivities();
    }

    @GetMapping("/{id}")
    public TimestampEntity getTimestampById(@PathVariable("id") UUID id) {
        return timestampService.findById(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@RequestBody TimestampEntity timestampEntity) {
        if (timestampEntity != null) {
            timestampService.insert(timestampEntity);
            return "Added a timestamp";
        } else {
            return "Request does not contain a body";
        }
        // return new RedirectView("/activity");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable UUID id) {
        if (id != null) {
            if (timestampService.delete(id)) {
                return "Deleted the timestamp.";
            } else {
                return "Cannot delete the timestamp.";
            }
        }
        return "The id is invalid for the timestamp.";
        // return new RedirectView("/activity");
    }

    @RequestMapping("/update")
    public String update(@RequestBody TimestampEntity timestampEntity) {
        if (timestampEntity != null) {
            timestampService.update(timestampEntity);
            return "Updated timestamp.";
        } else {
            return "Request does not contain a body";
        }
        // return new RedirectView("/activity");
    }
}
