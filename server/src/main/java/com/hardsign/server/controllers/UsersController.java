package com.hardsign.server.controllers;

import com.hardsign.server.exceptions.ConflictException;
import com.hardsign.server.exceptions.NotFoundException;
import com.hardsign.server.mappers.Mapper;
import com.hardsign.server.models.users.AddUserModel;
import com.hardsign.server.models.users.UserModel;
import com.hardsign.server.services.user.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UsersController {

    private final UserService userService;
    private final Mapper mapper;

    public UsersController(UserService userService, Mapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("login/{login}")
    public UserModel getUserByLogin(@PathVariable String login) {
        return userService.findUserByLogin(login)
                .map(mapper::mapToModel)
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping("{id}")
    public UserModel getUserById(@PathVariable long id) {
        return userService.findById(id)
                .map(mapper::mapToModel)
                .orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public UserModel addUser(@RequestBody AddUserModel addUserModel) {
        if (userService.findUserByLogin(addUserModel.getLogin()).isPresent())
            throw new ConflictException("User with same login exists.");

        var result = userService.create(addUserModel.getName(), addUserModel.getLogin(), addUserModel.getPassword());
        return mapper.mapToModel(result);
    }
}
