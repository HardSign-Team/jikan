package com.hardsign.server.services.timestamps;

import com.hardsign.server.models.timestamps.Timestamp;

import java.util.List;
import java.util.Optional;

public interface TimestampsService {
    List<Timestamp> findAllTimestamps(long activityId);

    Optional<Timestamp> findById(long id);

    Optional<Timestamp> findActiveTimestamp(long activityId);

    Timestamp save(Timestamp timestamp);

    void delete(long id);

    Optional<Timestamp> getLast(long activityId);
}
