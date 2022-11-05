package org.hardsign.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hardsign.models.auth.TelegramUserMeta;

@Getter
@AllArgsConstructor
public class BotRequest<TRequest> {
    private final TRequest request;
    private final TelegramUserMeta userMeta;
}
