package com.hardsign.server.repositories;


import com.hardsign.server.models.activities.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivitiesRepository extends JpaRepository<ActivityEntity, Long> {
    List<ActivityEntity> findActivityEntitiesByUserId(long userId);
}