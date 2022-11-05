package org.hardsign.models;

import lombok.Builder;
import lombok.Data;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.users.UserState;

@Data
@Builder
public class UpdateContext {
    private boolean isRegistered;
    private TelegramUserMeta meta;
    private UserState state;
    private long activityId;
}
