package com.hardsign.server.models.timestamps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimestampModel {
    private long id;
    private long activityId;
    private Date start;
    @Nullable
    private Date end;
}
