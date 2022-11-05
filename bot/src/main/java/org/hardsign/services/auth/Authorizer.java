package org.hardsign.services.auth;

import org.hardsign.models.auth.TelegramUserAuthMeta;
import org.hardsign.models.users.UserDto;

public interface Authorizer {
    String authorizeBot();
    String authorizeUser(TelegramUserAuthMeta meta);
    UserDto createUser(TelegramUserAuthMeta meta) throws Exception;
}
