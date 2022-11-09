package org.hardsign.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAuthMeta {
    private String login;
    private String name;
}
