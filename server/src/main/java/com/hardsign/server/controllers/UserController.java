package com.hardsign.server.controllers;

import com.hardsign.server.models.users.AddUserModel;
import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.models.users.UserModel;
import com.hardsign.server.repositories.UserRepository;
import com.hardsign.server.services.auth.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public UserController(UserRepository userRepository, PasswordService passwordService){
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @GetMapping("{login}")
    public ResponseEntity<UserModel> getUser(@PathVariable String login){
        var user = userRepository.findFirstByLogin(login);

        return user
                .map(x -> new UserModel(x.getName(), x.getLogin()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserEntity> addUser(@RequestBody AddUserModel addUserModel){
        var user = new UserEntity();
        user.setName(addUserModel.getName());
        user.setLogin(addUserModel.getLogin());
        user.setHashedPassword(passwordService.hash(addUserModel.getPassword()));

        var result = userRepository.save(user);

        return ResponseEntity.ok(result);
    }
}
