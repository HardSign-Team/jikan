package com.hardsign.server.mappers;

import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityEntity;
import com.hardsign.server.models.activities.ActivityModel;
import com.hardsign.server.models.timestamps.Timestamp;
import com.hardsign.server.models.timestamps.TimestampEntity;
import com.hardsign.server.models.timestamps.TimestampModel;
import org.springframework.stereotype.Component;

@Component
public class MapperImpl implements Mapper {

    @Override
    public Activity map(ActivityEntity entity) {
        return new Activity(entity.getId(), entity.getUserId(), entity.getName());
    }

    @Override
    public ActivityModel mapToModel(Activity activity) {
        return new ActivityModel(activity.getId(), activity.getUserId(), activity.getName());
    }

    @Override
    public ActivityEntity mapToEntity(Activity activity) {
        return new ActivityEntity(activity.getId(), activity.getUserId(), activity.getName());
    }

    @Override
    public Timestamp map(TimestampEntity entity) {
        return new Timestamp(entity.getId(), entity.getActivityId(), entity.getStart(), entity.getEnd());
    }

    @Override
    public TimestampModel mapToModel(Timestamp timestamp) {
        return new TimestampModel(timestamp.getId(), timestamp.getActivityId(), timestamp.getStart(), timestamp.getEnd());
    }

    @Override
    public TimestampEntity mapToEntity(Timestamp timestamp) {
        return new TimestampEntity(timestamp.getId(), timestamp.getActivityId(), timestamp.getStart(), timestamp.getEnd());
    }
}
