package com.hardsign.server.mappers;

import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityEntity;
import com.hardsign.server.models.activities.ActivityModel;
import com.hardsign.server.models.timestamps.Timestamp;
import com.hardsign.server.models.timestamps.TimestampEntity;
import com.hardsign.server.models.timestamps.TimestampModel;
import com.hardsign.server.models.users.User;
import com.hardsign.server.models.users.UserEntity;
import org.springframework.stereotype.Component;

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
        return new User(entity.getId(), entity.getName(), entity.getLogin());
    }

    @Override
    public UserEntity mapToEntity(User user) {
        var entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setLogin(user.getLogin());
        return entity;
    }
}
