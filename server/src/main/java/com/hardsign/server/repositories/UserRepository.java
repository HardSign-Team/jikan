package com.hardsign.server.repositories;

import com.hardsign.server.models.users.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

//TODO (lunev.d): uncomment after fix database
/*@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    UserEntity findByLogin(String Login);
}*/
