package org.hardsign.clients;

import okhttp3.OkHttpClient;
import org.hardsign.models.activities.ActivityDto;

public class JikanApiClientImpl implements JikanApiClient {

    private final ActivitiesClientImpl activitiesClient;

    public JikanApiClientImpl(OkHttpClient client) {
        activitiesClient = new ActivitiesClientImpl(client);
    }

    @Override
    public ActivitiesClient activities() {
        return activitiesClient;
    }
}

