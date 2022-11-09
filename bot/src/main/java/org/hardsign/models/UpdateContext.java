package org.hardsign.models;

import lombok.Builder;
import lombok.Data;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.State;
import org.jetbrains.annotations.Nullable;

@Data
@Builder
public class UpdateContext {
    private boolean isRegistered;
    private TelegramUserMeta meta;
    private State state;
    @Nullable
    private UserDto user;
    private long activityId;
}
