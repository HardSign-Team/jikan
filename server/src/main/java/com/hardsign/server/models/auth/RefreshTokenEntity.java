package com.hardsign.server.models.auth;

import lombok.Builder;

import javax.persistence.*;

@Entity
@Table(name = "tokens")
@Builder
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name="login", length = 32, nullable = false)
    public String userLogin;

    @Column(name="token", length = 1024, nullable = false)
    public String refreshToken;

    @Column(name="active", nullable = false)
    public boolean active;

    public RefreshTokenEntity() {}

    public RefreshTokenEntity(long id) {
        this(id, null, null, false);
    }

    public RefreshTokenEntity(long id, String login, String token, boolean active) {
        this.id = id;
        this.userLogin = login;
        this.refreshToken = token;
        this.active = active;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getUserLogin(){
        return userLogin;
    }

    public void setUserLogin(String userLogin){
        this.userLogin = userLogin;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public boolean getActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }
}
