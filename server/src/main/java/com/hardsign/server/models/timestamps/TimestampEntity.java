package com.hardsign.server.models.timestamps;

import com.hardsign.server.models.activities.ActivityEntity;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "timestamps")
public class TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(targetEntity = ActivityEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="activity_id", nullable = false)
    private ActivityEntity activity;

    @Column(name = "start_at", nullable = false)
    private ZonedDateTime start;

    @Nullable
    @Column(name = "end_at")
    private ZonedDateTime end;

    public TimestampEntity() { }

    public TimestampEntity(long id, ActivityEntity activity, ZonedDateTime start, @Nullable ZonedDateTime end) {
        this.id = id;
        this.activity = activity;
        this.start = start;
        this.end = end;
    }

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

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    @Nullable
    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(@Nullable ZonedDateTime end) {
        this.end = end;
    }
}

