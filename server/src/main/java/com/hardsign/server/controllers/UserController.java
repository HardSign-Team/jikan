package com.hardsign.server.controllers;

import com.hardsign.server.exceptions.ConflictException;
import com.hardsign.server.exceptions.NotFoundException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.users.AddUserModel;
import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.models.users.UserModel;
import com.hardsign.server.repositories.UserRepository;
import com.hardsign.server.services.auth.PasswordService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final Mapper mapper;

    public UserController(
            UserRepository userRepository,
            PasswordService passwordService,
            Mapper mapper) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.mapper = mapper;
    }

    @GetMapping("login/{login}")
    public UserModel getUserByLogin(@PathVariable String login){
        var userEntity = userRepository.findFirstByLogin(login)
                .orElseThrow(NotFoundException::new);

        var user = mapper.map(userEntity);

        return mapper.mapToModel(user);
    }

    @GetMapping("{id}")
    public UserModel getUserById(@PathVariable long id){
        var userEntity = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        var user = mapper.map(userEntity);

        return mapper.mapToModel(user);
    }

    @PostMapping
    public UserModel addUser(@RequestBody AddUserModel addUserModel){
        var user = UserEntity.builder()
                .name(addUserModel.getName())
                .login(addUserModel.getLogin())
                .hashedPassword(passwordService.hash(addUserModel.getPassword()))
                .build();

        if (userRepository.findFirstByLogin(addUserModel.getLogin()).isPresent())
            throw new ConflictException("User with same login exists.");

        var result = userRepository.save(user);
        return mapper.mapToModel(mapper.map(result));
    }
}
