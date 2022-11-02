package org.hardsign.clients;

import org.hardsign.models.JikanResponse;
import org.hardsign.models.activities.ActivityDto;

public interface ActivitiesClient {
    JikanResponse<ActivityDto[]> getAll();
}

