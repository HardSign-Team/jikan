package com.hardsign.server.Services.Timestamp;

import com.hardsign.server.models.timestamps.TimestampEntity;
import com.hardsign.server.repositories.TimestampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TimestampService implements ITimestampService {
    @Autowired
    TimestampRepository repository;

    @Override
    public List<TimestampEntity> findAllActivities() {
        return (List<TimestampEntity>)repository.findAll();
    }

    @Override
    public TimestampEntity findById(UUID id) {
        Optional<TimestampEntity> result = repository.findById(id);
        return result.orElse(null);
    }

    @Override
    public TimestampEntity insert(TimestampEntity p) {
        return repository.save(p);
    }

    @Override
    public boolean delete(UUID id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(TimestampEntity p) {
        try {
            repository.save(p);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
