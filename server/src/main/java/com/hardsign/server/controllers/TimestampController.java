package com.hardsign.server.controllers;

import com.hardsign.server.models.timestamps.TimestampEntity;
import com.hardsign.server.models.timestamps.TimestampModel;
import com.hardsign.server.services.Timestamp.TimestampService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/timestamp")
public class TimestampController {
    final TimestampService timestampService;

    public TimestampController(TimestampService timestampService) {
        this.timestampService = timestampService;
    }

    @GetMapping()
    public List<TimestampEntity> getAllTimestamps() {
        return timestampService.findAllActivities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimestampModel> getTimestampById(@PathVariable("id") String id) {
        var timestamp = Optional.of(timestampService.findById(UUID.fromString(id)));
        return timestamp
                .map(x -> new TimestampModel(x.Id.toString(), x.ActivityId.toString(), x.Start, x.End))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> create(@RequestBody TimestampModel timestampModel) {
        if (timestampModel == null) {
            return ResponseEntity.badRequest().body("Request does not contain a body");
        }

        if (timestampService.insert(getTimestampEntityByTimestampModel(timestampModel))) {
            return ResponseEntity.ok("Timestamp created");
        }

        return ResponseEntity.badRequest().body("Can't updated");
    }

    @GetMapping(value = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("The id is invalid for the timestamp");
        }

        if (timestampService.delete(UUID.fromString(id))) {
            return ResponseEntity.ok("Timestamp deleted");
        }

        return ResponseEntity.badRequest().body("Can't delete");
    }

    @GetMapping("/update")
    public ResponseEntity<String> update(@RequestBody TimestampModel timestampModel) {
        if (timestampModel == null) {
            return ResponseEntity.badRequest().body("Request does not contain a body");
        }

        if (timestampService.update(getTimestampEntityByTimestampModel(timestampModel))) {
            return ResponseEntity.ok("Timestamp created");
        }

        return ResponseEntity.badRequest().body("Can't updated");
    }

    private TimestampEntity getTimestampEntityByTimestampModel(TimestampModel timestampModel) {
        var entity = new TimestampEntity();
        entity.End = timestampModel.End;
        entity.Start = timestampModel.Start;
        entity.ActivityId = UUID.fromString(timestampModel.ActivityId);
        entity.Id = UUID.fromString(timestampModel.Id);
        return entity;
    }
}
