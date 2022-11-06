package org.hardsign.services.users;

import com.pengrad.telegrambot.model.User;
import org.hardsign.models.users.UserState;
import org.hardsign.models.users.UserStateEntity;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.repositories.UserStateRepository;

import java.util.function.Consumer;
import java.util.function.Function;

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
    public void update(User user, UserStatePatch patch) {
        var entity = getState(user);
        applyPatch(patch, UserStatePatch::getDeleteActivityId, entity::setDeleteActivityId);
        applyPatch(patch, UserStatePatch::getState, entity::setState);
        applyPatch(patch, UserStatePatch::getActivityId, entity::setActivityId);
        repository.save(entity);
    }

    @Override
    public void setState(User user, UserState state) {
        update(user, UserStatePatch.builder().state(state).build());
    }

    @Override
    public void setActivity(User user, long activityId) {
        update(user, UserStatePatch.builder().activityId(activityId).build());
    }

    @Override
    public void setDeleteActivity(User user, long activityId) {
        update(user, UserStatePatch.builder().deleteActivityId(activityId).build());
    }

    private <TProperty> void applyPatch(
            UserStatePatch patch,
            Function<UserStatePatch, TProperty> propertyProvider,
            Consumer<TProperty> consumer) {
        var property = propertyProvider.apply(patch);
        if (property != null) {
            consumer.accept(property);
        }
    }
}
