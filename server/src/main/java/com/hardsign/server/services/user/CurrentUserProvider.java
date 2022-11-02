package com.hardsign.server.services.user;

import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserProvider implements ICurrentUserProvider {

    private final UserRepository userRepository;


    public CurrentUserProvider(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public Optional<UserEntity> getCurrentUser() {
        var login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByLogin(login);
    }
}
