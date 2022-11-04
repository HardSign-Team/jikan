package com.hardsign.server.services.user;

import com.hardsign.server.models.users.User;
import com.hardsign.server.models.users.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CurrentUserProvider {
    Optional<User> getCurrentUser();
}
