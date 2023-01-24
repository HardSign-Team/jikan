package org.hardsign.clients.timestamps;

import org.hardsign.models.JikanResponse;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.*;

public interface TimestampsClient {
    JikanResponse<TimestampDto[]> getAll(BotRequest<GetAllTimestampsRequest> request);
    JikanResponse<TimestampDto> getById(BotRequest<GetTimestampByIdRequest> request);
    JikanResponse<TimestampDto> getLast(BotRequest<GetLastTimestampByActivityIdRequest> request);
    JikanResponse<TimestampDto> start(BotRequest<StartActivityRequest> request);
    JikanResponse<TimestampDto> stop(BotRequest<StopActivityRequest> request);
    JikanResponse<TimestampDto> add(BotRequest<AddTimestampRequest> request);
    JikanResponse<?> delete(BotRequest<DeleteTimestampByIdRequest> request);
}