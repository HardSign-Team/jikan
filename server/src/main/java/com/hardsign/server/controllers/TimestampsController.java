package com.hardsign.server.controllers;

import com.hardsign.server.exceptions.BadRequestException;
import com.hardsign.server.exceptions.ConflictException;
import com.hardsign.server.exceptions.ForbiddenException;
import com.hardsign.server.exceptions.NotFoundException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.timestamps.Timestamp;
import com.hardsign.server.models.timestamps.TimestampModel;
import com.hardsign.server.models.timestamps.TimestampPatch;
import com.hardsign.server.models.timestamps.requests.*;
import com.hardsign.server.models.users.User;
import com.hardsign.server.services.activities.ActivitiesService;
import com.hardsign.server.services.time.TimeProvider;
import com.hardsign.server.services.timestamps.TimestampsService;
import com.hardsign.server.services.user.CurrentUserProvider;
import com.hardsign.server.utils.users.UserUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.GET}, allowCredentials = "true")
@RequestMapping(value = "/api/timestamps/")
public class TimestampsController {
    private final CurrentUserProvider currentUserProvider;
    private final ActivitiesService activitiesService;
    private final TimestampsService timestampService;
    private final TimeProvider timeProvider;
    private final Mapper mapper;

    public TimestampsController(
            CurrentUserProvider currentUserProvider,
            ActivitiesService activitiesService,
            TimestampsService timestampService,
            TimeProvider timeProviderService,
            Mapper mapper) {
        this.currentUserProvider = currentUserProvider;
        this.activitiesService = activitiesService;
        this.timestampService = timestampService;
        this.timeProvider = timeProviderService;
        this.mapper = mapper;
    }

    @PostMapping("getAll")
    public List<TimestampModel> getAllTimestamps(@Valid @RequestBody GetAllTimestampsRequest request) {
        var user = getUserOrThrow();

        var activity = getActivityOrThrow(request.getActivityId());

        validateHasAccess(user, activity);

        return timestampService.findAllTimestamps(activity.getId())
                .stream()
                .map(mapper::mapToModel)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public TimestampModel getTimestampById(@PathVariable("id") long id) {
        var user = getUserOrThrow();

        var timestamp = timestampService.findById(id)
                .orElseThrow(NotFoundException::new);

        var activity = getActivityOrThrow(timestamp.getActivityId());

        validateHasAccess(user, activity);

        return mapper.mapToModel(timestamp);
    }

    @GetMapping("getAll/{activityId}")
    public List<TimestampModel> getAllTimestampsByActivityId(@PathVariable("activityId") long activityId) {
        var user = getUserOrThrow();

        var activity = getActivityOrThrow(activityId);

        validateHasAccess(user, activity);

        return timestampService.findAllTimestamps(activityId).stream().map(mapper::mapToModel).collect(Collectors.toList());
    }

    @GetMapping("newest/{activityId}")
    public TimestampModel getLastTimestampByActivityId(@PathVariable("activityId") long activityId) {
        var user = getUserOrThrow();

        var activity = getActivityOrThrow(activityId);

        validateHasAccess(user, activity);

        var timestamp = timestampService.getLast(activityId)
                .orElseThrow(NotFoundException::new);

        return mapper.mapToModel(timestamp);
    }

    @PostMapping(value = "start")
    public TimestampModel start(@Valid @RequestBody StartTimestampRequest request) {
        var user = getUserOrThrow();

        var activity = getActivityOrThrow(request.getActivityId());

        validateHasAccess(user, activity);

        var lastTimestamp = timestampService.findActiveTimestamp(activity.getId());
        if (lastTimestamp.isPresent())
            throw new ConflictException("Active timestamp not completed.");

        var timestamp = timestampService.save(new Timestamp(activity.getId(), timeProvider.now()))
                .orElseThrow(ConflictException::new);

        return mapper.mapToModel(timestamp);
    }

    @PostMapping(value = "stop")
    public TimestampModel stop(@Valid @RequestBody StopTimestampRequest request) {
        var user = getUserOrThrow();

        var activity = getActivityOrThrow(request.getActivityId());

        validateHasAccess(user, activity);

        var lastTimestamp = timestampService.findActiveTimestamp(activity.getId())
                .orElseThrow(() -> new NotFoundException("Active timestamp not found"));
        lastTimestamp.setEnd(timeProvider.now());

        var timestamp = timestampService.save(lastTimestamp)
                .orElseThrow(ConflictException::new);

        return mapper.mapToModel(timestamp);
    }

    @PutMapping("add")
    public TimestampModel add(@Valid @RequestBody AddTimestampRequest request) {
        var user = getUserOrThrow();

        var activity = getActivityOrThrow(request.getActivityId());

        validateHasAccess(user, activity);

        var from = request.getStart().toInstant(ZoneOffset.UTC);
        var to = request.getEnd().toInstant(ZoneOffset.UTC);

        validateDateRange(from, to);

        var timestamp = timestampService.add(new Timestamp(activity.getId(), from, to))
                .orElseThrow(ConflictException::new);

        return mapper.mapToModel(timestamp);
    }

    @PutMapping("edit")
    public TimestampModel edit(@Valid @RequestBody EditTimestampRequest request) {
        var user = getUserOrThrow();

        if (request.getStart() == null && request.getEnd() == null)
            throw new BadRequestException("Nothing changed.");

        var timestamp = timestampService.findById(request.getTimestampId())
                .orElseThrow(NotFoundException::new);

        if (timestamp.getEnd() == null)
            throw new BadRequestException("Can not edit active timestamps.");

        var activity = getActivityOrThrow(timestamp.getActivityId());

        validateHasAccess(user, activity);

        var patch = createTimestampPatch(request);

        var edited = patch.apply(timestamp);

        validateDateRange(edited.getStart(), edited.getEnd());

        var saved = timestampService.save(edited)
                .orElseThrow(ConflictException::new);

        return mapper.mapToModel(saved);
    }

    private static TimestampPatch createTimestampPatch(EditTimestampRequest request) {
        return TimestampPatch.builder()
                .start(Optional.ofNullable(request.getStart()).map(x -> x.toInstant(ZoneOffset.UTC)).orElse(null))
                .end(Optional.ofNullable(request.getEnd()).map(x -> x.toInstant(ZoneOffset.UTC)).orElse(null))
                .build();
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

    private Activity getActivityOrThrow(long activityId) {
        return activitiesService.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity not found."));
    }

    private static void validateHasAccess(User user, Activity activity) {
        if (!user.hasAccess(activity))
            throw new ForbiddenException("Has not access to activity.");
    }

    private static void validateDateRange(Instant from, Instant to) {
        if (from.isAfter(to))
            throw new BadRequestException("From-date should be less than to-date.");
    }
}
