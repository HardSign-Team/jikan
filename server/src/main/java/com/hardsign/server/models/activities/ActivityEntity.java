package com.hardsign.server.models.activities;

import com.hardsign.server.models.users.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "activities")
public class ActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    public ActivityEntity() { }

    public ActivityEntity(long id) {
        this(id, null, null);
    }

    public ActivityEntity(long userId, String name) {
        this(0, new UserEntity(userId), name);
    }

    public ActivityEntity(long id, UserEntity user, String name) {
        this.id = id;
        this.user = user;
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

