package org.hardsign.clients.activities;

import org.hardsign.models.JikanResponse;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.*;
import org.hardsign.models.requests.BotRequest;

public interface ActivitiesClient {
    JikanResponse<ActivityDto[]> getAll(BotRequest<GetAllActivitiesRequest> request);
    JikanResponse<ActivityDto> getById(BotRequest<GetActivityByIdRequest> request);
    JikanResponse<ActivityDto> create(BotRequest<CreateActivityRequest> request);
    JikanResponse<ActivityDto> update(BotRequest<UpdateActivityRequest> request);
    JikanResponse<?> delete(BotRequest<DeleteActivityRequest> request);
}

