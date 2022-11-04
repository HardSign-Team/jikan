package org.hardsign.clients.timestamps;

import okhttp3.OkHttpClient;
import org.hardsign.clients.RpcBaseClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.*;
import org.hardsign.services.Authorizer;

import java.util.function.Supplier;

public class TimestampsClientImpl extends RpcBaseClient implements TimestampsClient {
    public TimestampsClientImpl(
            OkHttpClient client,
            Authorizer authorizer,
            Supplier<BotSettings> settingsProvider) {
        super(client, "timestamps", authorizer, settingsProvider);
    }

    @Override
    public JikanResponse<TimestampDto[]> getAll(GetAllTimestampsRequest request) {
        return post("getAll", request, TimestampDto[].class);
    }

    @Override
    public JikanResponse<TimestampDto> getById(GetTimestampByIdRequest request) {
        return get(Long.toString(request.getTimestampId()), TimestampDto.class);
    }

    @Override
    public JikanResponse<TimestampDto> start(StartActivityRequest request) {
        return post("start", request, TimestampDto.class);
    }

    @Override
    public JikanResponse<TimestampDto> stop(StopActivityRequest request) {
        return post("stop", request, TimestampDto.class);
    }

    @Override
    public JikanResponse<?> delete(DeleteTimestampByIdRequest request) {
        return delete(Long.toString(request.getActivityId()), null, Object.class);
    }
}
