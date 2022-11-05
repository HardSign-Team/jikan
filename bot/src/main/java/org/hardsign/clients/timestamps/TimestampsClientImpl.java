package org.hardsign.clients.timestamps;

import okhttp3.OkHttpClient;
import org.hardsign.clients.BotBaseClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.*;
import org.hardsign.services.Authorizer;

import java.util.function.Supplier;

public class TimestampsClientImpl extends BotBaseClient implements TimestampsClient {
    public TimestampsClientImpl(
            OkHttpClient client,
            Authorizer authorizer,
            Supplier<BotSettings> settingsProvider) {
        super(client, "api/timestamps", authorizer, settingsProvider);
    }

    @Override
    public JikanResponse<TimestampDto[]> getAll(BotRequest<GetAllTimestampsRequest> request) {
        return post("getAll", request, TimestampDto[].class);
    }

    @Override
    public JikanResponse<TimestampDto> getById(BotRequest<GetTimestampByIdRequest> request) {
        return get(Long.toString(request.getRequest().getTimestampId()), request, TimestampDto.class);
    }

    @Override
    public JikanResponse<TimestampDto> start(BotRequest<StartActivityRequest> request) {
        return post("start", request, TimestampDto.class);
    }

    @Override
    public JikanResponse<TimestampDto> stop(BotRequest<StopActivityRequest> request) {
        return post("stop", request, TimestampDto.class);
    }

    @Override
    public JikanResponse<?> delete(BotRequest<DeleteTimestampByIdRequest> request) {
        return delete(Long.toString(request.getRequest().getActivityId()), request, Object.class);
    }
}
