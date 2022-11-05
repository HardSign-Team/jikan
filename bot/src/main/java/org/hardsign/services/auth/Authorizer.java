package org.hardsign.services.auth;

import org.hardsign.models.auth.TelegramUserMeta;

public interface Authorizer {
    String authorizeBot();
    String authorizeUser(TelegramUserMeta meta);
}
