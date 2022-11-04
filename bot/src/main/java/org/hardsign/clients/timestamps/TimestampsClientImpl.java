package org.hardsign.clients.timestamps;

import okhttp3.OkHttpClient;
import org.hardsign.clients.RpcBaseClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.GetAllTimestampsRequest;
import org.hardsign.models.timestamps.requests.StartActivityRequest;
import org.hardsign.models.timestamps.requests.StopActivityRequest;
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
    public JikanResponse<TimestampDto> getById(long id) {
        return get(Long.toString(id), TimestampDto.class);
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
    public JikanResponse<?> delete(long id) {
        return delete(Long.toString(id), null, Object.class);
    }
}
