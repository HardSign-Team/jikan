package org.hardsign.clients;

import org.hardsign.clients.activities.ActivitiesClient;
import org.hardsign.clients.timestamps.TimestampsClient;
import org.hardsign.clients.users.UsersClient;

public interface JikanApiClient {
    ActivitiesClient activities();
    TimestampsClient timestamps();
    UsersClient users();
}

