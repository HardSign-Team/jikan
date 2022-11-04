package org.hardsign.clients.activities;

import okhttp3.OkHttpClient;
import org.hardsign.clients.RpcBaseClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.*;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.services.Authorizer;

import java.util.function.Supplier;

public class ActivitiesClientImpl extends RpcBaseClient implements ActivitiesClient {
    public ActivitiesClientImpl(OkHttpClient client, Authorizer authorizer, Supplier<BotSettings> settingsProvider) {
        super(client, "activities", authorizer, settingsProvider);
    }

    @Override
    public JikanResponse<ActivityDto[]> getAll(GetAllActivitiesRequest request) {
        return get("", ActivityDto[].class);
    }

    @Override
    public JikanResponse<ActivityDto> getById(GetActivityByIdRequest request) {
        return get(Long.toString(request.getActivityId()), ActivityDto.class);
    }

    @Override
    public JikanResponse<ActivityDto> create(CreateActivityRequest request) {
        return post("create", request, ActivityDto.class);
    }

    @Override
    public JikanResponse<ActivityDto> update(UpdateActivityRequest request) {
        return patch("", request, ActivityDto.class);
    }

    @Override
    public JikanResponse<?> delete(DeleteActivityRequest request) {
        return delete(Long.toString(request.getActivityId()), null, Object.class);
    }
}

