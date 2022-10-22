package com.hardsign.server.models.timestamps;

import com.hardsign.server.models.activities.ActivityEntity;
import net.bytebuddy.utility.nullability.MaybeNull;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "timestamps")
public class TimeStampEntity {

    @javax.persistence.Id
    public UUID Id;

    @ManyToOne(targetEntity = ActivityEntity.class)
    @JoinColumn(name="activity_id", nullable = false)
    public UUID ActivityId;

    @Column(name = "start_at", nullable = false)
    public Date Start;

    @MaybeNull
    @Column(name = "end_at")
    public Date End;

}
