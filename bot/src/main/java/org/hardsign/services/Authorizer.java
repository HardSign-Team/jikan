package org.hardsign.services;

import org.hardsign.models.auth.TelegramUserAuthMeta;

public interface Authorizer {
    String authorizeBot();
    String authorizeUser(TelegramUserAuthMeta meta);
}
