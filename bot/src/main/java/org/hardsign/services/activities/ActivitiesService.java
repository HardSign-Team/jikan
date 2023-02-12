package org.hardsign.services.activities;

import org.hardsign.clients.JikanApiClient;
import org.hardsign.models.DateRange;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.ActivityOverviewDto;
import org.hardsign.models.activities.ActivityTotalTimeDto;
import org.hardsign.models.activities.requests.*;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.StartActivityRequest;
import org.hardsign.models.timestamps.requests.StopActivityRequest;

import java.util.Optional;

public class ActivitiesService {
    private final JikanApiClient jikanApiClient;

    public ActivitiesService(JikanApiClient jikanApiClient) {
        this.jikanApiClient = jikanApiClient;
    }

    public void deleteActivity(long activityId, TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new DeleteActivityRequest(activityId), meta);
        jikanApiClient.activities().delete(request).ensureSuccess();
    }

    public Optional<ActivityDto> findActivity(long activityId, TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new GetActivityByIdRequest(activityId), meta);
        var response = jikanApiClient.activities().getById(request);
        return response.notFound() ? Optional.empty() : Optional.ofNullable(response.getValueOrThrow());
    }

    public ActivityOverviewDto[] getActivitiesOverview(TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new GetOverviewAllActivitiesRequest(), meta);
        return jikanApiClient.activities().getOverviewAll(request).getValueOrThrow();
    }

    public ActivityTotalTimeDto getTotalTime(long activityId, DateRange dateRange, TelegramUserMeta meta) throws Exception {
        var apiRequest = new GetActivityTotalTimeRequest(activityId, dateRange.getFrom(), dateRange.getTo());
        var botRequest = new BotRequest<>(apiRequest, meta);
        return jikanApiClient.activities().getTotalTime(botRequest).getValueOrThrow();
    }

    public TimestampDto startTracking(long activityId, TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new StartActivityRequest(activityId), meta);
        return jikanApiClient.timestamps().start(request).getValueOrThrow();
    }

    public TimestampDto stopTracking(long activityId, TelegramUserMeta meta) throws Exception {
        var stopRequest = new BotRequest<>(new StopActivityRequest(activityId), meta);
        return jikanApiClient.timestamps().stop(stopRequest).getValueOrThrow();
    }

    public ActivityDto create(String name, TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new CreateActivityRequest(name), meta);
        return jikanApiClient.activities().create(request).getValueOrThrow();
    }
}
