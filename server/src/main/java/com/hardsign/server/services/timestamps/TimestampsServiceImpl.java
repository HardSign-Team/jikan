package com.hardsign.server.services.timestamps;

import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.SortField;
import com.hardsign.server.models.timestamps.FindTimestampsArgs;
import com.hardsign.server.models.timestamps.Timestamp;
import com.hardsign.server.models.timestamps.TimestampField;
import com.hardsign.server.models.users.User;
import com.hardsign.server.repositories.TimestampsRepository;
import com.hardsign.server.utils.Validation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimestampsServiceImpl implements TimestampsService {
    private final TimestampsRepository repository;
    private final Mapper mapper;

    public TimestampsServiceImpl(TimestampsRepository timestampsRepository, Mapper mapper) {
        this.repository = timestampsRepository;
        this.mapper = mapper;
    }

    public List<Timestamp> findAllTimestamps(long activityId) {
        return repository.findTimestampEntitiesByActivityIdOrderByStartAsc(activityId)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    public Optional<Timestamp> findById(long id) {
        return repository.findById(id).map(mapper::map);
    }

    @Override
    public Page<Timestamp> findTimestamps(FindTimestampsArgs args) {
        var result = repository.findTimestampsByFilter(
                args.getActivityId(),
                java.sql.Timestamp.from(args.getFrom()),
                java.sql.Timestamp.from(args.getTo()),
                PageRequest.of(args.getPage(), args.getPageSize(), Sort.by(createSort(args.getSortBy()))));

        return result.map(mapper::map);
    }

    @Override
    @Transactional
    public Validation<Timestamp> save(Timestamp timestamp) {
        return validateNotIntersect(timestamp)
                .map(mapper::mapToEntity)
                .map(repository::save)
                .map(mapper::map);
    }

    @Override
    public Optional<Timestamp> findActiveTimestamp(long activityId) {
        return repository.findFirstByActivityIdAndEndIsNull(activityId)
                .map(mapper::map);
    }

    @Override
    public List<Timestamp> findAllActiveTimestamps(User user) {
        return repository.findTimestampEntitiesByActivity_User_IdAndEndIsNull(user.getId())
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Timestamp> getLast(long activityId) {
        return repository.findTopByActivityIdOrderByStartDesc(activityId).map(mapper::map);
    }

    @Override
    public Duration getTotalTime(long id, Instant from, Instant to) {
        var seconds = repository.getTotalTimeByActivityId(id, from, to, null);
        return Duration.ofSeconds(seconds);
    }

    private Validation<Timestamp> validateNotIntersect(Timestamp timestamp) {
        var seconds = repository.getTotalTimeByActivityId(
                timestamp.getActivityId(),
                timestamp.getStart(),
                timestamp.getEnd(),
                timestamp.getId());
        return seconds > 0
                ? Validation.invalid("Another timestamp found in range.")
                : Validation.valid(timestamp);
    }

    private List<Sort.Order> createSort(List<SortField<TimestampField>> sortBy) {
        return sortBy.stream().distinct().map(SortField::toSortOrder).collect(Collectors.toList());
    }

}
