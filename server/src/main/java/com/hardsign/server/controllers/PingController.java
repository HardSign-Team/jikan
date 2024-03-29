package com.hardsign.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PingController {

    @GetMapping(value = "/ping")
    @ResponseBody
    public String ping(){
        return "pong";
    }

    @GetMapping(value = "/ping/authorized")
    @ResponseBody
    public String pingAuthorized(){
        return "pong";
    }
}