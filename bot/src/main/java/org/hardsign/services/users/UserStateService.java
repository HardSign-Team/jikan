package org.hardsign.services.users;

import com.pengrad.telegrambot.model.User;
import org.hardsign.models.users.State;
import org.hardsign.models.users.UserState;
import org.hardsign.models.users.UserStatePatch;

public interface UserStateService {
    UserState getState(User user);
    UserState getState(long userId);
    void update(User user, UserStatePatch patch);
    void setState(User user, State state);
    void setActivity(User user, long activityId);
}

