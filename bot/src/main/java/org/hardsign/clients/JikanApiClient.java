package org.hardsign.clients;

import org.hardsign.clients.activities.ActivitiesClient;
import org.hardsign.clients.timestamps.TimestampsClient;

public interface JikanApiClient {
    ActivitiesClient activities();
    TimestampsClient timestamps();
}

