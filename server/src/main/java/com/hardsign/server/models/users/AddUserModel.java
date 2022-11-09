package com.hardsign.server.models.users;

import lombok.Data;

@Data
public class AddUserModel {
    private String name;
    private String login;
    private String password;
}
