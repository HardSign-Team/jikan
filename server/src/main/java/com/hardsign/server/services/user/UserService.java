package com.hardsign.server.services.user;

import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.repositories.UserRepository;
import com.hardsign.server.services.auth.PasswordService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> getUser(String login) {
        return userRepository.findFirstByLogin(login);
    }
}
