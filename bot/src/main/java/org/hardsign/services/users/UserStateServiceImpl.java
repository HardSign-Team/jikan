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
                .orElseGet(() -> repository.save(new UserStateEntity(userId, UserState.None, 0, 0)));
    }

    @Override
    public void setState(User user, UserState state) {
        var entity = getState(user);
        entity.setState(state);
        repository.save(entity);
    }

    @Override
    public void setActivity(User user, long activityId) {
        var entity = getState(user);
        entity.setActivityId(activityId);
        repository.save(entity);
    }

    @Override
    public void setDeleteActivity(User user, long activityId) {
        var entity = getState(user);
        entity.setDeleteActivityId(activityId);
        repository.save(entity);
    }
}
