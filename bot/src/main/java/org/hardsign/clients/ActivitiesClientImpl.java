package org.hardsign.clients;

import okhttp3.OkHttpClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.activities.ActivityDto;

public class ActivitiesClientImpl extends RpcBaseClient implements ActivitiesClient {
    protected ActivitiesClientImpl(OkHttpClient client) {
        super(client, "activities");
    }

    @Override
    public JikanResponse<ActivityDto[]> getAll() {
        return get("", ActivityDto[].class);
    }
}

