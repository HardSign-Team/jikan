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

import java.util.Optional;
import java.util.function.Function;

public interface Mapper {
    Activity map(ActivityEntity entity);
    ActivityModel mapToModel(Activity activity);
    ActivityOverviewModel mapToOverviewModel(Activity activity, Function<Long, Optional<Timestamp>> activeTimestampsProvider);
    ActivityEntity mapToEntity(Activity activity);

    Timestamp map(TimestampEntity entity);
    TimestampModel mapToModel(Timestamp timestamp);
    TimestampEntity mapToEntity(Timestamp timestamp);

    User map(UserEntity entity);
    UserModel mapToModel(User user);

    JwtTokensModel mapToModel(JwtTokens tokens);
}

