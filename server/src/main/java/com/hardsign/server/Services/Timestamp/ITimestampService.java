package com.hardsign.server.Services.Timestamp;

import com.hardsign.server.models.timestamps.TimestampEntity;

import java.util.List;
import java.util.UUID;

public interface ITimestampService {
    List<TimestampEntity> findAllActivities();

    TimestampEntity findById(UUID id);

    TimestampEntity insert(TimestampEntity p);

    boolean delete(UUID id);

    boolean update(TimestampEntity p);
}
