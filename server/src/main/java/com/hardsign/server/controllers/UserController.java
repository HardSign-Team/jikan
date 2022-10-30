package com.hardsign.server.controllers;

import com.hardsign.server.models.users.AddUserModel;
import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.models.users.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//TODO (lunev.d): uncomment after fix database
/*@RestController("api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("{login}")
    public ResponseEntity<UserModel> getUser(@PathVariable String login){
        var user = Optional
                .of(userRepository.findByLogin(login));

        return user
                .map(x -> new UserModel(x.Name, x.Login))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public void addUser(@RequestBody AddUserModel addUserModel){
        var user = new UserEntity();
        user.Name = addUserModel.getName();
        user.Login = addUserModel.getLogin();
        user.HashedPassword = addUserModel.getPassword();

        userRepository.save(user);
    }
}*/
