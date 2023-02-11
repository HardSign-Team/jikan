package org.hardsign.services.users;

import com.pengrad.telegrambot.model.User;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.models.users.UserState;
import org.hardsign.models.users.UserStatePatch;

public class ContextBoundedUserStateServiceImpl implements ContextBoundedUserStateService {
    private final UserStateServiceInternal userStateService;
    private final UpdateContext context;

    public ContextBoundedUserStateServiceImpl(UserStateServiceInternal userStateService, UpdateContext context) {
        this.userStateService = userStateService;
        this.context = context;
    }

    @Override
    public UserState getState(User user) {
        return userStateService.getState(user);
    }

    @Override
    public UserState getState(long userId) {
        return userStateService.getState(userId);
    }

    @Override
    public void update(User user, UserStatePatch patch) {
        userStateService.update(user, patch);
        context.setState(patch.getState());
    }

    @Override
    public void setState(User user, State state) {
        update(user, UserStatePatch.builder().state(state).build());
    }

    @Override
    public void setActivity(User user, long activityId) {
        userStateService.update(user, UserStatePatch.builder().activityId(activityId).build());
    }
}
