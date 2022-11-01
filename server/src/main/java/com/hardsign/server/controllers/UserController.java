package com.hardsign.server.controllers;

import com.hardsign.server.models.users.AddUserModel;
import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.models.users.UserModel;
import com.hardsign.server.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//TODO (lunev.d): uncomment after fix database
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("{login}")
    public ResponseEntity<UserModel> getUser(@PathVariable String login){
        var user = userRepository.findByLogin(login);

        return user
                .map(x -> new UserModel(x.getName(), x.getLogin()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public void addUser(@RequestBody AddUserModel addUserModel){
        var user = new UserEntity();
        user.setName(addUserModel.getName());
        user.setLogin(addUserModel.getLogin());
        user.setHashedPassword(addUserModel.getPassword()); // TODO: 01.11.2022 why???

        userRepository.save(user);
    }
}
