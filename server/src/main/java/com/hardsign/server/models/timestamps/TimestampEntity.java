package com.hardsign.server.models.timestamps;

import com.hardsign.server.models.activities.ActivityEntity;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "timestamps")
public class TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="activity_id", nullable = false, foreignKey = @ForeignKey(name = "fk_activity"))
    private ActivityEntity activity;

    @Column(name = "start_at", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Timestamp start;

    @Nullable
    @Column(name = "end_at", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Timestamp end;

    public TimestampEntity() { }

    public TimestampEntity(long id, ActivityEntity activity, Timestamp start, @Nullable Timestamp end) {
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

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    @Nullable
    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(@Nullable Timestamp end) {
        this.end = end;
    }
}

