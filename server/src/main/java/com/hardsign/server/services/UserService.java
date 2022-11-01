package com.hardsign.server.services;

import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordService passwordService){
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> getUser(String login){
        return userRepository.findByLogin(login);
    }
}
