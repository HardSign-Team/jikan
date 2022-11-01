package com.hardsign.server.models.activities;

import com.hardsign.server.models.users.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "activities")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    public ActivityEntity() { }

    public ActivityEntity(long id, long userId, String name) {
        this.id = id;
        this.userId = userId;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

