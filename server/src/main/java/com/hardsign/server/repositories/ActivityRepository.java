package com.hardsign.server.repositories;


import com.hardsign.server.models.activities.ActivityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivityRepository extends CrudRepository<ActivityEntity, UUID> {
}
