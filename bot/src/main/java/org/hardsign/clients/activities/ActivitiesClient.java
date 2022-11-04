package org.hardsign.clients.activities;

import org.hardsign.models.JikanResponse;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.CreateActivityRequest;
import org.hardsign.models.activities.UpdateActivityRequest;

public interface ActivitiesClient {
    JikanResponse<ActivityDto[]> getAll();
    JikanResponse<ActivityDto> getById(long id);
    JikanResponse<ActivityDto> create(CreateActivityRequest request);
    JikanResponse<ActivityDto> update(UpdateActivityRequest request);
    JikanResponse<?> delete(long id);
}

