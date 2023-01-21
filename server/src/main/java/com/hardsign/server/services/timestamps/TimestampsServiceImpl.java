package com.hardsign.server.services.timestamps;

import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.timestamps.Timestamp;
import com.hardsign.server.models.users.User;
import com.hardsign.server.repositories.TimestampsRepository;
import org.springframework.stereotype.Service;

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

    public Timestamp save(Timestamp timestamp) {
        var entity = mapper.mapToEntity(timestamp);

        var saved = repository.save(entity);

        return mapper.map(saved);
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
        var seconds = repository.getTotalTimeByActivityId(id, from, to);
        return Duration.ofSeconds(seconds);
    }
}
