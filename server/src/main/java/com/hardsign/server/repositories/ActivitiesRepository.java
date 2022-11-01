package com.hardsign.server.repositories;


import com.hardsign.server.models.activities.ActivityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivitiesRepository extends CrudRepository<ActivityEntity, Long> {
}
