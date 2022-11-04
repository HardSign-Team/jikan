package org.hardsign.clients;

import okhttp3.OkHttpClient;
import org.hardsign.clients.activities.ActivitiesClient;
import org.hardsign.clients.activities.ActivitiesClientImpl;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.services.Authorizer;

import java.util.function.Supplier;

public class JikanApiClientImpl implements JikanApiClient {

    private final ActivitiesClientImpl activitiesClient;

    public JikanApiClientImpl(OkHttpClient client, Authorizer authorizer, Supplier<BotSettings> settingsProvider) {
        activitiesClient = new ActivitiesClientImpl(client, authorizer, settingsProvider);
    }

    @Override
    public ActivitiesClient activities() {
        return activitiesClient;
    }
}

