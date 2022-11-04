package org.hardsign.clients.timestamps;

import org.hardsign.models.JikanResponse;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.GetAllTimestampsRequest;
import org.hardsign.models.timestamps.requests.StartActivityRequest;
import org.hardsign.models.timestamps.requests.StopActivityRequest;

public interface TimestampsClient {
    JikanResponse<TimestampDto[]> getAll(GetAllTimestampsRequest request);
    JikanResponse<TimestampDto> getById(long id);
    JikanResponse<TimestampDto> start(StartActivityRequest request);
    JikanResponse<TimestampDto> stop(StopActivityRequest request);
    JikanResponse<?> delete(long id);
}