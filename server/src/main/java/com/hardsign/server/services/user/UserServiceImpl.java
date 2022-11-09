package com.hardsign.server.services.user;

import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.users.User;
import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.models.users.UserRole;
import com.hardsign.server.repositories.UserRepository;
import com.hardsign.server.services.auth.PasswordService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final PasswordService passwordService;
    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserServiceImpl(PasswordService passwordService, UserRepository userRepository, Mapper mapper) {
        this.passwordService = passwordService;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findFirstByLogin(login).map(mapper::map);
    }

    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id).map(mapper::map);
    }

    @Override
    public User create(String name, String login, String password) {
        var entity = UserEntity.builder()
                .name(name)
                .login(login)
                .role(UserRole.USER)
                .hashedPassword(passwordService.hash(password))
                .build();
        return mapper.map(userRepository.save(entity));
    }
}
