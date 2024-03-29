package org.hardsign.models.timestamps.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTimestampByIdRequest {
    private long timestampId;
}
