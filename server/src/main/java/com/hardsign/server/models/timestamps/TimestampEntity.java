package com.hardsign.server.models.timestamps;

import com.hardsign.server.models.activities.ActivityEntity;
import net.bytebuddy.utility.nullability.MaybeNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "timestamps")
public class TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(targetEntity = ActivityEntity.class)
    @JoinColumn(name="activity_id", nullable = false)
    private long activityId;

    @Column(name = "start_at", nullable = false)
    private Date start;

    @MaybeNull
    @Column(name = "end_at")
    private Date end;

    public TimestampEntity(long id, long activityId, Date start, @MaybeNull Date end) {
        this.id = id;
        this.activityId = activityId;
        this.start = start;
        this.end = end;
    }

    public TimestampEntity() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @MaybeNull
    public Date getEnd() {
        return end;
    }

    public void setEnd(@MaybeNull Date end) {
        this.end = end;
    }
}

