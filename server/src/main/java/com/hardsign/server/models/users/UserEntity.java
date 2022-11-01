package com.hardsign.server.models.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {

    @javax.persistence.Id
    public long Id;

    @Column(name="name", length = 32, nullable = false)
    public String Name;

    @Column(name="hashed_password", nullable = false)
    public String HashedPassword;

}
