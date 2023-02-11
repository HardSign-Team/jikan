package org.hardsign.models;

import lombok.Builder;
import lombok.Data;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.timestamps.TimestampDto;
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
    @Nullable
    private ActivityDto activity;
    @Nullable
    private TimestampDto activeTimestamp;

    public boolean isActiveTimestamp(long timestampId) {
        return activeTimestamp != null && activeTimestamp.getId() == timestampId;
    }

    public boolean hasSelectedActivity() {
        return activity != null;
    }

    public boolean hasActiveTimestamp() {
        return activeTimestamp != null;
    }
}
