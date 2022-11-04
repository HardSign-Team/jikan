package com.hardsign.server.services.timestamps;

import com.hardsign.server.exceptions.DomainException;
import com.hardsign.server.models.timestamps.Timestamp;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TimestampsService {
    List<Timestamp> findAllTimestamps(long activityId);

    Optional<Timestamp> findById(long id);

    Timestamp start(long activityId, Date currentDate) throws DomainException;

    Timestamp stop(long activityId, Date currentDate) throws DomainException;

    void delete(long id);
}
