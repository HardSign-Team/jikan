package org.hardsign.clients.activities;

import org.hardsign.models.JikanResponse;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.*;

public interface ActivitiesClient {
    JikanResponse<ActivityDto[]> getAll(GetAllActivitiesRequest request);
    JikanResponse<ActivityDto> getById(GetActivityByIdRequest request);
    JikanResponse<ActivityDto> create(CreateActivityRequest request);
    JikanResponse<ActivityDto> update(UpdateActivityRequest request);
    JikanResponse<?> delete(DeleteActivityRequest request);
}

