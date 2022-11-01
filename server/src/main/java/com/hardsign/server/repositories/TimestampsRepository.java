package com.hardsign.server.repositories;

import com.hardsign.server.models.timestamps.TimestampEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimestampsRepository extends CrudRepository<TimestampEntity, Long> {
    Optional<TimestampEntity> findFirstByActivityIdAndEndIsNull(long ActivityId);
}
