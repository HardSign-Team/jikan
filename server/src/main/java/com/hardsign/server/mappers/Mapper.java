package com.hardsign.server.mappers;

import com.hardsign.server.models.activities.Activity;
import com.hardsign.server.models.activities.ActivityEntity;
import com.hardsign.server.models.activities.ActivityModel;
import com.hardsign.server.models.timestamps.Timestamp;
import com.hardsign.server.models.timestamps.TimestampEntity;
import com.hardsign.server.models.timestamps.TimestampModel;
import com.hardsign.server.models.users.User;
import com.hardsign.server.models.users.UserEntity;

public interface Mapper {
    Activity map(ActivityEntity entity);
    ActivityModel mapToModel(Activity activity);
    ActivityEntity mapToEntity(Activity activity);

    Timestamp map(TimestampEntity entity);
    TimestampModel mapToModel(Timestamp timestamp);
    TimestampEntity mapToEntity(Timestamp timestamp);

    User map(UserEntity entity);
    UserEntity mapToEntity(User user);
}

