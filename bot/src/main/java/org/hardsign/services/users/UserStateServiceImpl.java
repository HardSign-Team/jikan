package org.hardsign.services.users;

import com.pengrad.telegrambot.model.User;
import org.hardsign.models.users.State;
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
    public UserState getState(User user) {
        return getState(user.id());
    }

    @Override
    public UserState getState(long userId) {
        return repository.findByUserId(userId)
                .map(this::map)
                .orElseGet(() -> map(createDefaultState(userId)));
    }

    @Override
    public void update(User user, UserStatePatch patch) {
        var entity = repository.findByUserId(user.id()).orElseGet(() -> createDefaultState(user.id()));
        applyPatch(patch, UserStatePatch::getDeleteActivityId, entity::setDeletionActivityId);
        applyPatch(patch, UserStatePatch::getState, entity::setState);
        applyPatch(patch, UserStatePatch::getActivityId, entity::setActivityId);
        repository.save(entity);
    }

    @Override
    public void setState(User user, State state) {
        update(user, UserStatePatch.builder().state(state).build());
    }

    @Override
    public void setActivity(User user, long activityId) {
        update(user, UserStatePatch.builder().activityId(activityId).build());
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

    private UserStateEntity createDefaultState(long userId) {
        return repository.save(new UserStateEntity(0, userId, State.None, 0, 0));
    }

    private UserState map(UserStateEntity entity) {
        return new UserState(
                entity.getUserId(),
                entity.getState(),
                entity.getActivityId(),
                entity.getDeletionActivityId());
    }
}
