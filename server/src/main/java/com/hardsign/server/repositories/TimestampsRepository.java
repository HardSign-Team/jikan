package com.hardsign.server.repositories;

import com.hardsign.server.models.timestamps.TimestampEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
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
            "select t from TimestampEntity t " +
            "where t.activity.id = :activityId " +
            "and t.start < :to " +
            "and coalesce(t.end, current_timestamp) > :from ")
    Page<TimestampEntity> findTimestampsByFilter(
            @Param("activityId") long activityId,
            @Param("from") @NotNull Timestamp from,
            @Param("to") @NotNull Timestamp to,
            Pageable pageable);

    @Query(value =
            "select coalesce(extract(epoch from sum(least(coalesce(end_at, current_timestamp), coalesce(:to, current_timestamp)) - greatest(start_at, :from))), 0) from timestamps " +
            "where activity_id = :activityId " +
            "and start_at < coalesce(:to, current_timestamp) " +
            "and coalesce(end_at, current_timestamp) > :from " +
            "and (:exceptTimestampId is null or id != :exceptTimestampId)", nativeQuery = true)
    Long getTotalTimeByActivityId(
            @Param("activityId") long activityId,
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("exceptTimestampId") Long exceptTimestampId);
}
