package com.hardsign.server.models.timestamps;

import com.hardsign.server.models.activities.ActivityEntity;
import net.bytebuddy.utility.nullability.MaybeNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "timestamps")
public class TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @ManyToOne(targetEntity = ActivityEntity.class)
    @JoinColumn(name="activity_id", nullable = false)
    private ActivityEntity activity;

    @Column(name = "start_at", nullable = false)
    private Date start;

    @Nullable
    @Column(name = "end_at")
    private Date end;

    public TimestampEntity(long id, ActivityEntity activity, Date start, @MaybeNull Date end) {
        this.id = id;
        this.activity = activity;
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

    public ActivityEntity getActivity() {
        return activity;
    }

    public void setActivity(ActivityEntity activity) {
        this.activity = activity;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @Nullable
    public Date getEnd() {
        return end;
    }

    public void setEnd(@Nullable Date end) {
        this.end = end;
    }
}

