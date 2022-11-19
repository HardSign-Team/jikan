package com.hardsign.server.mappers;

import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityEntity;
import com.hardsign.server.models.activities.ActivityModel;
import com.hardsign.server.models.activities.ActivityOverviewModel;
import com.hardsign.server.models.auth.JwtTokens;
import com.hardsign.server.models.auth.JwtTokensModel;
import com.hardsign.server.models.timestamps.Timestamp;
import com.hardsign.server.models.timestamps.TimestampEntity;
import com.hardsign.server.models.timestamps.TimestampModel;
import com.hardsign.server.models.users.User;
import com.hardsign.server.models.users.UserEntity;
import com.hardsign.server.models.users.UserModel;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class MapperImpl implements Mapper {

    @Override
    public Activity map(ActivityEntity entity) {
        return new Activity(entity.getId(), entity.getUser().getId(), entity.getName());
    }

    @Override
    public ActivityModel mapToModel(Activity activity) {
        return new ActivityModel(activity.getId(), activity.getUserId(), activity.getName());
    }

    @Override
    public ActivityOverviewModel mapToOverviewModel(Activity x, Function<Long, Optional<Timestamp>> activeTimestampsProvider) {
        var activeTimestamp = activeTimestampsProvider
                .apply(x.getId())
                .map(this::mapToModel)
                .orElse(null);
        return new ActivityOverviewModel(x.getId(), x.getUserId(), x.getName(), activeTimestamp);
    }

    @Override
    public ActivityEntity mapToEntity(Activity activity) {
        final var user = new UserEntity();
        user.setId(activity.getUserId());
        return new ActivityEntity(activity.getId(), user, activity.getName());
    }

    @Override
    public Timestamp map(TimestampEntity entity) {
        return new Timestamp(entity.getId(), entity.getActivity().getId(), entity.getStart(), entity.getEnd());
    }

    @Override
    public TimestampModel mapToModel(Timestamp timestamp) {
        return new TimestampModel(timestamp.getId(), timestamp.getActivityId(), timestamp.getStart(), timestamp.getEnd());
    }

    @Override
    public TimestampEntity mapToEntity(Timestamp timestamp) {
        var activityEntity = new ActivityEntity();
        activityEntity.setId(timestamp.getActivityId());
        return new TimestampEntity(timestamp.getId(), activityEntity, timestamp.getStart(), timestamp.getEnd());
    }

    @Override
    public User map(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getLogin(),
                entity.getHashedPassword(),
                entity.getRole());
    }

    @Override
    public UserEntity mapToEntity(User user) {
        var entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setLogin(user.getLogin());
        return entity;
    }

    @Override
    public UserModel mapToModel(User user) {
        return new UserModel(user.getId(), user.getName(), user.getLogin());
    }

    @Override
    public JwtTokensModel mapToModel(JwtTokens tokens) {
        return new JwtTokensModel(tokens.getAccessToken(), tokens.getRefreshToken());
    }
}
