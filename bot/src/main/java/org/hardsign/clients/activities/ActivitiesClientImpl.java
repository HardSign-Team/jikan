package org.hardsign.clients.activities;

import okhttp3.OkHttpClient;
import org.hardsign.clients.BotBaseClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.*;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.services.auth.Authorizer;

import java.util.function.Supplier;

public class ActivitiesClientImpl extends BotBaseClient implements ActivitiesClient {
    public ActivitiesClientImpl(OkHttpClient client, Authorizer authorizer, Supplier<BotSettings> settingsProvider) {
        super(client, "api/activities", authorizer, settingsProvider);
    }

    @Override
    public JikanResponse<ActivityDto[]> getAll(BotRequest<GetAllActivitiesRequest> request) {
        return get("", request, ActivityDto[].class);
    }

    @Override
    public JikanResponse<ActivityDto> getById(BotRequest<GetActivityByIdRequest> request) {
        return get(Long.toString(request.getRequest().getActivityId()), request, ActivityDto.class);
    }

    @Override
    public JikanResponse<ActivityDto> create(BotRequest<CreateActivityRequest> request) {
        return post("create", request, ActivityDto.class);
    }

    @Override
    public JikanResponse<ActivityDto> update(BotRequest<UpdateActivityRequest> request) {
        return patch("", request, ActivityDto.class);
    }

    @Override
    public JikanResponse<?> delete(BotRequest<DeleteActivityRequest> request) {
        return delete(Long.toString(request.getRequest().getActivityId()), request, Object.class);
    }
}

