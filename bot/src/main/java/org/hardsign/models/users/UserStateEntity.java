package org.hardsign.models.users;


import jakarta.persistence.*;

@Entity
@Table(name = "users_states")
public class UserStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "state", nullable = false)
    private State state;

    @Column(name = "current_activity_id")
    private long activityId;

    @Column(name = "deletion_activity_id")
    private long deletionActivityId;

    @Column(name = "custom_data")
    private String stateDataJson;

    public UserStateEntity() {}

    public UserStateEntity(long userId, State state, String stateDataJson) {
        this(0, userId, state, 0, 0, stateDataJson);
    }

    public UserStateEntity(
            long id,
            long userId,
            State state,
            long activityId,
            long deletionActivityId,
            String customDataJson) {
        this.id = id;
        this.userId = userId;
        this.state = state;
        this.activityId = activityId;
        this.deletionActivityId = deletionActivityId;
        this.stateDataJson = customDataJson;
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

    public String getStateDataJson() {
        return stateDataJson;
    }

    public void setStateDataJson(String stateDataJson) {
        this.stateDataJson = stateDataJson;
    }
}
