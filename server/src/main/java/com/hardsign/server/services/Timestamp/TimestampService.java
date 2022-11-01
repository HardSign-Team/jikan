package com.hardsign.server.services.Timestamp;

import com.hardsign.server.models.timestamps.TimestampEntity;
import com.hardsign.server.repositories.TimestampRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class TimestampService implements ITimestampService {
    final TimestampRepository repository;

    public TimestampService(TimestampRepository repository) {
        this.repository = repository;
    }

    public List<TimestampEntity> findAllActivities() {
        return (List<TimestampEntity>)repository.findAll();
    }

    public TimestampEntity findById(UUID id) {
        var result = repository.findById(id);
        return result.orElse(null);
    }

    public boolean insert(TimestampEntity timestampEntity) {
        try {
            repository.save(timestampEntity);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean delete(UUID id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

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
