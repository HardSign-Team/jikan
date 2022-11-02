package com.hardsign.server.controllers;

import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.services.user.ICurrentUserProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class PingController {

    private final ICurrentUserProvider currentUserProvider;

    public PingController(ICurrentUserProvider currentUserProvider){

        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping(value = "/ping")
    @ResponseBody
    public String ping(){
        return "pong";
    }

    @GetMapping(value = "/ping2")
    @ResponseBody
    public String ping2(){
        return "pong";
    }

    @GetMapping("/ping3")
    @ResponseBody
    public Optional<UserEntity> ping3() {
        return currentUserProvider.getCurrentUser();
    }
}