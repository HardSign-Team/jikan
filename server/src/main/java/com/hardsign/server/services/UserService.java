package com.hardsign.server.services;

import com.hardsign.server.models.users.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final List<UserEntity> users;

    public UserService(PasswordService passwordService){
        var user = new UserEntity();
        user.setLogin("I");
        user.Id = UUID.randomUUID();
        user.Name = "hate";
        user.HashedPassword = passwordService.Hash("java");

        users = List.of(
                user
        );
    }

    public Optional<UserEntity> getUser(String login){
        return users.stream()
                .filter(x -> login.equals(x.getLogin()))
                .findFirst();
    }
}
