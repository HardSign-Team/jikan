package org.hardsign.services.users;

import com.pengrad.telegrambot.model.User;
import org.hardsign.models.users.UserState;
import org.hardsign.models.users.UserStateEntity;
import org.hardsign.models.users.UserStatePatch;

public interface UserStateService {
    UserStateEntity getState(User user);
    UserStateEntity getState(long userId);
    void update(User user, UserStatePatch patch);
    void setState(User user, UserState state);
    void setActivity(User user, long activityId);
    void setDeleteActivity(User user, long activityId);
}

