package com.hardsign.server.repositories;

import com.hardsign.server.models.timestamps.TimestampEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimestampsRepository extends JpaRepository<TimestampEntity, Long> {
    Optional<TimestampEntity> findFirstByActivityIdAndEndIsNull(long activityId);

    Optional<TimestampEntity> findTopByActivityIdOrderByStartDesc(long activityId);

    List<TimestampEntity> findTimestampEntitiesByActivityIdOrderByStartAsc(long activityId);

    List<TimestampEntity> findTimestampEntitiesByActivity_User_IdAndEndIsNull(long userId);

    @Query(value =
            "select coalesce(extract(epoch from sum(coalesce(end_at, current_timestamp) - start_at)), 0) from timestamps " +
            "where activity_id = :activityId " +
            "AND :from <= start_at " +
            "AND coalesce(end_at, current_timestamp) <= :to", nativeQuery = true)
    Long getTotalTimeByActivityId(@Param("activityId") long activityId, @Param("from") Instant from, @Param("to") Instant to);
}
