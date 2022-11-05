package org.hardsign.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hardsign.models.auth.TelegramUserAuthMeta;

@Getter
@AllArgsConstructor
public abstract class BotRequest<TRequest> {
    private final TRequest request;
    private final TelegramUserAuthMeta userMeta;
}
