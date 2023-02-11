package org.hardsign.services.users;

import com.pengrad.telegrambot.model.User;
import org.hardsign.models.UpdateContext;

public interface UserStateService extends UserStateServiceInternal {
    ContextBoundedUserStateService with(UpdateContext context);

    void setActivity(User user, long activityId);
}

