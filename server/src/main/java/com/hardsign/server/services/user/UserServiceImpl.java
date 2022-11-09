package com.hardsign.server.services.user;

import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.users.User;
import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserServiceImpl(UserRepository userRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<UserEntity> getUserByLogin(String login) {
        return userRepository.findFirstByLogin(login);
    }

    @Override
    public Optional<UserEntity> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(UserEntity user) {
        return mapper.map(userRepository.save(user));
    }
}
