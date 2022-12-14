package com.hardsign.server.models.users;

import lombok.Builder;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name="name", length = 32, nullable = false)
    private String name;

    @Column(name="login", length = 32, nullable = false)
    private String login;

    @Column(name="hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name="role")
    private UserRole role;

    public UserEntity() {}

    public UserEntity(long id) {
        this(id, null, null, null, UserRole.USER);
    }

    public UserEntity(long id, String name, String login, String hashedPassword, UserRole role) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
