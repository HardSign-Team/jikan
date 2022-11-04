package com.hardsign.server.controllers;

import com.hardsign.server.exceptions.NotFoundException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.timestamps.TimestampModel;
import com.hardsign.server.models.timestamps.requests.GetAllTimestampsRequest;
import com.hardsign.server.models.timestamps.requests.StartTimestampRequest;
import com.hardsign.server.models.timestamps.requests.StopTimestampRequest;
import com.hardsign.server.models.users.User;
import com.hardsign.server.services.activities.ActivitiesService;
import com.hardsign.server.services.time.TimeProviderService;
import com.hardsign.server.services.timestamps.TimestampsService;
import com.hardsign.server.services.user.CurrentUserProvider;
import com.hardsign.server.utils.activities.ActivitiesUtils;
import com.hardsign.server.utils.users.UserUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/timestamps/")
public class TimestampsController {
    private final CurrentUserProvider currentUserProvider;
    private final ActivitiesService activitiesService;
    private final TimestampsService timestampService;
    private final TimeProviderService timeProviderService;
    private final Mapper mapper;

    public TimestampsController(
            CurrentUserProvider currentUserProvider,
            ActivitiesService activitiesService,
            TimestampsService timestampService,
            TimeProviderService timeProviderService,
            Mapper mapper) {
        this.currentUserProvider = currentUserProvider;
        this.activitiesService = activitiesService;
        this.timestampService = timestampService;
        this.timeProviderService = timeProviderService;
        this.mapper = mapper;
    }

    @PostMapping("getAll")
    public List<TimestampModel> getAllTimestamps(@Valid @RequestBody GetAllTimestampsRequest request) {
        var user = getUserOrThrow();

        var activity = activitiesService.findById(request.getActivityId())
                .filter(ActivitiesUtils.isOwnedBy(user))
                .orElseThrow(NotFoundException::new);

        return timestampService.findAllTimestamps(activity.getUserId())
                .stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity<TimestampModel> getTimestampById(@PathVariable("id") long id) {
        return timestampService.findById(id)
                .map(mapper::mapToModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "start")
    @ResponseBody
    public ResponseEntity<Object> start(@RequestBody StartTimestampRequest request) {
        var activityId = request.getActivityId();
        var currentDate = timeProviderService.getCurrentDate();
        try {
            // TODO: 01.11.2022 validate
            var timestamp = timestampService.start(activityId, currentDate);
            return ResponseEntity.ok(mapper.mapToModel(timestamp));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "stop")
    @ResponseBody
    public ResponseEntity<Object> stop(@RequestBody StopTimestampRequest request) {
        var activityId = request.getActivityId();
        var currentDate = timeProviderService.getCurrentDate();
        try {
            var timestamp = timestampService.stop(activityId, currentDate);
            return ResponseEntity.ok(mapper.mapToModel(timestamp));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "delete/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        timestampService.delete(id);

        return ResponseEntity.ok("OK");
    }

    private User getUserOrThrow() {
        return UserUtils.getUserOrThrow(currentUserProvider);
    }
}
