package com.hardsign.server.models.activities;

import com.hardsign.server.models.timestamps.TimestampEntity;
import com.hardsign.server.models.users.UserEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "activities")
public class ActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user"))
    private UserEntity user;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private List<TimestampEntity> timestamps;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    public ActivityEntity() { }

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

