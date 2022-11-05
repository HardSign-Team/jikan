package com.hardsign.server.services.timestamps;

import com.hardsign.server.exceptions.DomainException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.timestamps.Timestamp;
import com.hardsign.server.models.timestamps.TimestampEntity;
import com.hardsign.server.repositories.TimestampsRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    public Timestamp start(long activityId, Date currentDate) throws DomainException {
        var lastTimestamp = repository.findFirstByActivityIdAndEndIsNull(activityId);
        if (lastTimestamp.isPresent())
            throw new DomainException("Active timestamp not completed.");

        var saved = repository.save(new TimestampEntity(activityId, currentDate));

        return mapper.map(saved);
    }

    @Override
    public Timestamp stop(long activityId, Date currentDate) throws DomainException {
        var lastTimestamp = repository.findFirstByActivityIdAndEndIsNull(activityId)
                .orElseThrow(() -> new DomainException("Active timestamp not found"));

        lastTimestamp.setEnd(currentDate);

        var saved = repository.save(lastTimestamp);
        return mapper.map(saved);
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Timestamp> getLast(long activityId) {
        return repository.findTopByActivityIdOrderByStartDesc(activityId).map(mapper::map);
    }
}
