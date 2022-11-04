package org.hardsign.clients;

import okhttp3.OkHttpClient;
import org.hardsign.clients.activities.ActivitiesClient;
import org.hardsign.clients.activities.ActivitiesClientImpl;
import org.hardsign.clients.timestamps.TimestampsClient;
import org.hardsign.clients.timestamps.TimestampsClientImpl;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.services.Authorizer;

import java.util.function.Supplier;

public class JikanApiClientImpl implements JikanApiClient {

    private final ActivitiesClientImpl activitiesClient;
    private final TimestampsClientImpl timestampsClient;

    public JikanApiClientImpl(OkHttpClient client, Authorizer authorizer, Supplier<BotSettings> settingsProvider) {
        activitiesClient = new ActivitiesClientImpl(client, authorizer, settingsProvider);
        timestampsClient = new TimestampsClientImpl(client, authorizer, settingsProvider);
    }

    @Override
    public ActivitiesClient activities() {
        return activitiesClient;
    }

    @Override
    public TimestampsClient timestamps() {
        return timestampsClient;
    }
}

