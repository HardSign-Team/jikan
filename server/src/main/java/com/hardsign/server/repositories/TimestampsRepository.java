package com.hardsign.server.repositories;

import com.hardsign.server.models.timestamps.TimestampEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimestampsRepository extends JpaRepository<TimestampEntity, Long> {
    Optional<TimestampEntity> findFirstByActivityIdAndEndIsNull(long ActivityId);
}