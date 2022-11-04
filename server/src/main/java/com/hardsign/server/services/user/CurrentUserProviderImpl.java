package com.hardsign.server.services.user;

import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.users.User;
import com.hardsign.server.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserProviderImpl implements CurrentUserProvider {

    private final UserRepository userRepository;
    private final Mapper mapper;

    public CurrentUserProviderImpl(UserRepository userRepository, Mapper mapper) {

        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public Optional<User> getCurrentUser() {
        var login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findFirstByLogin(login).map(mapper::map);
    }
}
