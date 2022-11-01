package com.hardsign.server.models.timestamps;

import com.hardsign.server.models.activities.ActivityEntity;
import net.bytebuddy.utility.nullability.MaybeNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "timestamps")
public class TimestampEntity {

    @javax.persistence.Id
    public long Id;

    @ManyToOne(targetEntity = ActivityEntity.class)
    @JoinColumn(name="activity_id", nullable = false)
    public int ActivityId;

    @Column(name = "start_at", nullable = false)
    public Date Start;

    @MaybeNull
    @Column(name = "end_at")
    public Date End;

}
