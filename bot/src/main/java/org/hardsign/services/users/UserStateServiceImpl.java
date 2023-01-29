package org.hardsign.services.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.User;
import org.hardsign.models.users.*;
import org.hardsign.repositories.UserStateRepository;

import java.util.function.Consumer;
import java.util.function.Function;

public class UserStateServiceImpl implements UserStateService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserStateRepository repository;
    private final String defaultStateDataJson;

    public UserStateServiceImpl(UserStateRepository repository) {
        this.repository = repository;
        this.defaultStateDataJson = toJsonSafety(StateData.empty());
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
        applyPatch(patch, UserStatePatch::getState, entity::setState);
        applyPatch(patch, UserStatePatch::getActivityId, entity::setActivityId);
        applyPatch(patch, UserStatePatch::getDeleteActivityId, entity::setDeletionActivityId);
        applyPatch(patch, UserStatePatch::getStateData, value -> entity.setStateDataJson(toJsonSafety(value)));
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
        return repository.save(new UserStateEntity(userId, State.None, defaultStateDataJson));
    }

    private UserState map(UserStateEntity entity) {
        return new UserState(
                entity.getUserId(),
                entity.getState(),
                entity.getActivityId(),
                entity.getDeletionActivityId(),
                getStateData(entity));
    }

    private StateData getStateData(UserStateEntity entity) {
        try {
            var stateDataJson = entity.getStateDataJson();
            if (stateDataJson == null)
                return StateData.empty();
            return objectMapper.readValue(stateDataJson, StateData.class);
        } catch (JsonProcessingException e) {
            return StateData.empty();
        }
    }

    private String toJsonSafety(StateData value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return defaultStateDataJson;
        }
    }
}
