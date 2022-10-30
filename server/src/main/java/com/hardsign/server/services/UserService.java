package com.hardsign.server.services;

import com.hardsign.server.models.users.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final List<UserEntity> users;

    public UserService(){
        var user = new UserEntity();
        user.Id = UUID.randomUUID();
        user.Login = "I";
        user.Name = "hate";
        user.HashedPassword = "java";

        users = List.of(
                user
        );
    }

    public Optional<UserEntity> getUser(String login){
        return users.stream()
                .filter(x -> login.equals(x.Login))
                .findFirst();
    }
}
