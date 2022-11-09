package org.hardsign.models.users;


import jakarta.persistence.*;

@Entity
@Table(name = "users_states")
public class UserStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "state")
    private State state;

    @Column(name = "current_activity_id")
    private long activityId;

    @Column(name = "deletion_activity_id")
    private long deletionActivityId;

    public UserStateEntity() {}

    public UserStateEntity(long id, long userId, State state, long activityId, long deletionActivityId) {
        this.id = id;
        this.userId = userId;
        this.state = state;
        this.activityId = activityId;
        this.deletionActivityId = deletionActivityId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public long getDeletionActivityId() {
        return deletionActivityId;
    }

    public void setDeletionActivityId(long deletionActivityId) {
        this.deletionActivityId = deletionActivityId;
    }
}
