package org.hardsign.models.users;

import lombok.Data;

@Data
public class UserDto {
    private long id;
    private String name;
    private String login;
}
