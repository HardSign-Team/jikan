package com.hardsign.server.models.activities;

import com.hardsign.server.models.users.UserEntity;

import javax.persistence.*;
import java.util.UUID;

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

    public ActivityEntity() { }

    public ActivityEntity(UUID id, UUID userId, String name) {
        this.Id = id;
        this.UserId = userId;
        this.Name = name;
    }
}
