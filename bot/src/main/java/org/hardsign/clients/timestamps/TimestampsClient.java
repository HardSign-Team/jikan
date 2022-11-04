package org.hardsign.clients.timestamps;

import org.hardsign.models.JikanResponse;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.*;

public interface TimestampsClient {
    JikanResponse<TimestampDto[]> getAll(GetAllTimestampsRequest request);
    JikanResponse<TimestampDto> getById(GetTimestampByIdRequest request);
    JikanResponse<TimestampDto> start(StartActivityRequest request);
    JikanResponse<TimestampDto> stop(StopActivityRequest request);
    JikanResponse<?> delete(DeleteTimestampByIdRequest request);
}