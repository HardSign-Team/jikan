package org.hardsign.services.timestamps;

import org.hardsign.clients.JikanApiClient;
import org.hardsign.models.DateRange;
import org.hardsign.models.HttpCodes;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.CreateTimestampResult;
import org.hardsign.models.timestamps.EditTimestampResult;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.*;

import java.util.Arrays;
import java.util.Optional;

public class TimestampsService {
    private final JikanApiClient jikanApiClient;

    public TimestampsService(JikanApiClient jikanApiClient) {

        this.jikanApiClient = jikanApiClient;
    }

    public void deleteTimestamp(long timestampId, TelegramUserMeta meta) throws Exception {
        var request = new DeleteTimestampByIdRequest(timestampId);
        var botRequest = new BotRequest<>(request, meta);
        jikanApiClient.timestamps().delete(botRequest).ensureSuccess();
    }

    public Optional<TimestampDto> findTimestamp(long timestampId, TelegramUserMeta meta) throws Exception {
        var request = new GetTimestampByIdRequest(timestampId);
        var botRequest = new BotRequest<>(request, meta);
        var timestamp = jikanApiClient.timestamps().getById(botRequest);
        return timestamp.notFound() ? Optional.empty() : Optional.ofNullable(timestamp.getValueOrThrow());
    }

    public TimestampDto[] findTimestamps(long activityId, DateRange dateRange, TelegramUserMeta meta) throws Exception {
        return findTimestamps(new FindTimestampsRequest(activityId, dateRange.getFrom(), dateRange.getTo()), meta);
    }

    public TimestampDto[] findTimestamps(FindTimestampsRequest findTimestampsRequest, TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(findTimestampsRequest, meta);
        var timestamps = jikanApiClient.timestamps().find(request).getValueOrThrow();
        return Arrays.stream(timestamps).sorted(TimestampDto::compareAscending).toArray(TimestampDto[]::new);
    }

    public Optional<TimestampDto> findActiveTimestamp(long activityId, TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new GetLastTimestampByActivityIdRequest(activityId), meta);
        var response = jikanApiClient.timestamps().getLast(request);
        return response.notFound() ? Optional.empty() : Optional.ofNullable(response.getValueOrThrow());
    }

    public CreateTimestampResult addTimestamp(long activityId, DateRange dateRange, TelegramUserMeta meta) throws Exception {
        var apiRequest = new AddTimestampRequest(activityId, dateRange.getFrom(), dateRange.getTo());
        var request = new BotRequest<>(apiRequest, meta);
        var result = jikanApiClient.timestamps().add(request);

        return HttpCodes.Conflict.is(result.getCode())
                ? new CreateTimestampResult(null, true)
                : new CreateTimestampResult(result.getValueOrThrow(), false);
    }

    public Optional<TimestampDto> getTimestamp(long timestampId, TelegramUserMeta meta) throws Exception {
        var request = new GetTimestampByIdRequest(timestampId);
        var botRequest = new BotRequest<>(request, meta);
        var response = jikanApiClient.timestamps().getById(botRequest);
        return response.notFound() ? Optional.empty() : Optional.ofNullable(response.getValueOrThrow());
    }

    public EditTimestampResult editTimestamp(long timestampId, DateRange dateRange, TelegramUserMeta meta) throws Exception {
        var request = new EditTimestampRequest(timestampId, dateRange.getFrom(), dateRange.getTo());
        var botRequest = new BotRequest<>(request, meta);
        var result = jikanApiClient.timestamps().edit(botRequest);
        return HttpCodes.Conflict.is(result.getCode())
                ? new EditTimestampResult(null, true)
                : new EditTimestampResult(result.getValueOrThrow(), false);
    }
}
