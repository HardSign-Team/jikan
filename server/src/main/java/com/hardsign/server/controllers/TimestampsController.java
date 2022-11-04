package com.hardsign.server.controllers;

import com.hardsign.server.exceptions.DomainException;
import com.hardsign.server.exceptions.ForbiddenException;
import com.hardsign.server.exceptions.NotFoundException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.timestamps.TimestampModel;
import com.hardsign.server.models.timestamps.requests.DeleteTimestampRequest;
import com.hardsign.server.models.timestamps.requests.GetAllTimestampsRequest;
import com.hardsign.server.models.timestamps.requests.StartTimestampRequest;
import com.hardsign.server.models.timestamps.requests.StopTimestampRequest;
import com.hardsign.server.models.users.User;
import com.hardsign.server.services.activities.ActivitiesService;
import com.hardsign.server.services.time.TimeProviderService;
import com.hardsign.server.services.timestamps.TimestampsService;
import com.hardsign.server.services.user.CurrentUserProvider;
import com.hardsign.server.utils.users.UserUtils;
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
                .filter(user::hasAccess)
                .orElseThrow(NotFoundException::new);

        return timestampService.findAllTimestamps(activity.getUserId())
                .stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public TimestampModel getTimestampById(@PathVariable("id") long id) {
        var user = getUserOrThrow();

        var timestamp = timestampService.findById(id)
                .orElseThrow(NotFoundException::new);

        var activity = activitiesService.findById(timestamp.getActivityId())
                .orElseThrow(NotFoundException::new);

        validateHasAccess(user, activity);

        return mapper.mapToModel(timestamp);
    }

    @PostMapping(value = "start")
    public TimestampModel start(@Valid @RequestBody StartTimestampRequest request) throws DomainException {
        var user = getUserOrThrow();

        var activity = activitiesService.findById(request.getActivityId())
                .orElseThrow(NotFoundException::new);

        validateHasAccess(user, activity);

        var timestamp = timestampService.start(activity.getId(), timeProviderService.getCurrentDate());
        return mapper.mapToModel(timestamp);
    }

    @PostMapping(value = "stop")
    public TimestampModel stop(@Valid @RequestBody StopTimestampRequest request) throws DomainException {
        var user = getUserOrThrow();

        var activity = activitiesService.findById(request.getActivityId())
                .orElseThrow(NotFoundException::new);

        validateHasAccess(user, activity);

        var timestamp = timestampService.stop(activity.getId(), timeProviderService.getCurrentDate());
        return mapper.mapToModel(timestamp);
    }

    @PostMapping(value = "delete")
    public void delete(@Valid @RequestBody DeleteTimestampRequest request) {
        var user = getUserOrThrow();

        var timestamp = timestampService.findById(request.getTimestampId())
                .orElseThrow(NotFoundException::new);

        var activity = activitiesService.findById(timestamp.getActivityId())
                        .orElseThrow(NotFoundException::new);

        validateHasAccess(user, activity);

        timestampService.delete(timestamp.getId());
    }

    private User getUserOrThrow() {
        return UserUtils.getUserOrThrow(currentUserProvider);
    }

    private static void validateHasAccess(User user, Activity activity) {
        if (!user.hasAccess(activity))
            throw new ForbiddenException("Has not access to activity.");
    }
}
