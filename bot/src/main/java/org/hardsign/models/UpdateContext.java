package org.hardsign.models;

import lombok.Builder;
import lombok.Data;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.UserState;
import org.jetbrains.annotations.Nullable;

@Data
@Builder
public class UpdateContext {
    private boolean isRegistered;
    private TelegramUserMeta meta;
    private UserState state;
    @Nullable
    private UserDto user;
    private long activityId;
}
