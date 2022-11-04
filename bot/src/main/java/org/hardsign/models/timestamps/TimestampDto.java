package org.hardsign.models.timestamps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimestampDto {
    private long id;
    private long activityId;
    private Date start;
    @Nullable
    private Date end;
}