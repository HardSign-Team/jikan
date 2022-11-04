package com.hardsign.server.models.users;

import lombok.Data;

@Data
public class UserModel {
    private final long id;
    private final String name;
    private final String login;

    public UserModel(long id, String name, String login){
        this.id = id;
        this.name = name;
        this.login = login;
    }
}
