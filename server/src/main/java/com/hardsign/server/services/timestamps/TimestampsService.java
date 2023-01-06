package com.hardsign.server.services.timestamps;

import com.hardsign.server.models.timestamps.Timestamp;
import com.hardsign.server.models.users.User;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TimestampsService {
    List<Timestamp> findAllTimestamps(long activityId);

    Optional<Timestamp> findById(long id);

    Optional<Timestamp> findActiveTimestamp(long activityId);

    List<Timestamp> findAllActiveTimestamps(User user);

    Timestamp save(Timestamp timestamp);

    void delete(long id);

    Optional<Timestamp> getLast(long activityId);

    Duration getTotalTime(long id, Instant from, Instant to);
}
