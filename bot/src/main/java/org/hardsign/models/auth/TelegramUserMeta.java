package org.hardsign.models.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TelegramUserMeta {
    private final long id;
    private final String login;
    private final String name;
}
