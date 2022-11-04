package com.hardsign.server.services.timestamps;

import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.activities.ActivityEntity;
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

    public List<Timestamp> findAllTimestamps() {
        return repository.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    public Optional<Timestamp> findById(long id) {
        return repository.findById(id).map(mapper::map);
    }

    @Override
    public Timestamp start(long activityId, Date currentDate) throws Exception {
        var lastTimestamp = repository.findFirstByActivityIdAndEndIsNull(activityId);
        if (lastTimestamp.isPresent())
            throw new Exception("Active timestamp found");
        var entity = new TimestampEntity(0, new ActivityEntity(activityId), currentDate, null);
        var saved = repository.save(entity);
        return mapper.map(saved);
    }

    @Override
    public Timestamp stop(long activityId, Date currentDate) throws Exception {
        var lastTimestamp = repository.findFirstByActivityIdAndEndIsNull(activityId);
        if (lastTimestamp.isEmpty())
            throw new Exception("Active timestamp not found");
        var entity = lastTimestamp.get();
        entity.setEnd(currentDate);
        var saved = repository.save(entity);
        return mapper.map(saved);
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }
}
