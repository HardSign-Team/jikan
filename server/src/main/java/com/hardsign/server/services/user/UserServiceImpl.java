package com.hardsign.server.services.user;

import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> getUserByLogin(String login) {
        return userRepository.findFirstByLogin(login);
    }
}
