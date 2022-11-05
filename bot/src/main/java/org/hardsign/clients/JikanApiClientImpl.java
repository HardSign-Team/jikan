package org.hardsign.clients;

import okhttp3.OkHttpClient;
import org.hardsign.clients.activities.ActivitiesClient;
import org.hardsign.clients.activities.ActivitiesClientImpl;
import org.hardsign.clients.timestamps.TimestampsClient;
import org.hardsign.clients.timestamps.TimestampsClientImpl;
import org.hardsign.clients.users.UsersClient;
import org.hardsign.clients.users.UsersClientImpl;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.services.auth.Authorizer;

import java.util.function.Supplier;

public class JikanApiClientImpl implements JikanApiClient {

    private final ActivitiesClient activitiesClient;
    private final TimestampsClient timestampsClient;
    private final UsersClient usersClient;

    public JikanApiClientImpl(OkHttpClient client, Authorizer authorizer, Supplier<BotSettings> settingsProvider) {
        activitiesClient = new ActivitiesClientImpl(client, authorizer, settingsProvider);
        timestampsClient = new TimestampsClientImpl(client, authorizer, settingsProvider);
        usersClient = new UsersClientImpl(client, authorizer, settingsProvider);
    }

    @Override
    public ActivitiesClient activities() {
        return activitiesClient;
    }

    @Override
    public TimestampsClient timestamps() {
        return timestampsClient;
    }

    @Override
    public UsersClient users() {
        return usersClient;
    }
}

