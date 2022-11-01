package com.hardsign.server.models.activities;

import com.hardsign.server.models.users.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "activities")
public class ActivityEntity {

    @javax.persistence.Id
    public long Id;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id", nullable = false)
    public int UserId;

    @Column(name = "name", length = 64, nullable = false)
    public String Name;
}
