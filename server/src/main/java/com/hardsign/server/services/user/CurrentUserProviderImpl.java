package com.hardsign.server.services.user;

import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.users.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserProviderImpl implements CurrentUserProvider {

    private final UserService userService;
    private final Mapper mapper;

    public CurrentUserProviderImpl(UserService userService, Mapper mapper) {

        this.userService = userService;
        this.mapper = mapper;
    }

    public Optional<User> getCurrentUser() {
        var login = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getUserByLogin(login).map(mapper::map);
    }
}
