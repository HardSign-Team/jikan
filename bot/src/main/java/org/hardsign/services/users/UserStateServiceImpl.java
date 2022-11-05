package org.hardsign.services.users;

import com.pengrad.telegrambot.model.User;
import org.hardsign.models.users.UserState;
import org.hardsign.models.users.UserStateEntity;
import org.hardsign.repositories.UserStateRepository;

public class UserStateServiceImpl implements UserStateService {

    private final UserStateRepository repository;

    public UserStateServiceImpl(UserStateRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserStateEntity getState(User user) {
        return getState(user.id());
    }

    @Override
    public UserStateEntity getState(long userId) {
        return repository.findByUserId(userId)
                .orElseGet(() -> repository.save(new UserStateEntity(userId, UserState.None, 0)));
    }

    @Override
    public UserStateEntity setState(User user, UserState state) {
        var entity = getState(user);
        entity.setState(state);
        return repository.save(entity);
    }

    @Override
    public UserStateEntity setActivity(User user, long activityId) {
        var entity = getState(user);
        entity.setActivityId(activityId);
        return repository.save(entity);
    }
}
