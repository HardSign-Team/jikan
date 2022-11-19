package com.hardsign.server.repositories;

import com.hardsign.server.models.timestamps.TimestampEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimestampsRepository extends JpaRepository<TimestampEntity, Long> {
    Optional<TimestampEntity> findFirstByActivityIdAndEndIsNull(long activityId);

    Optional<TimestampEntity> findTopByActivityIdOrderByStartDesc(long activityId);

    List<TimestampEntity> findTimestampEntitiesByActivityIdOrderByStartAsc(long activityId);

    List<TimestampEntity> findTimestampEntitiesByActivity_User_IdAndEndIsNull(long userId);
}
