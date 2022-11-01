package com.hardsign.server.models.auth;

public class RefreshJwtRequest {
    private String refreshToken;

    public String getRefreshToken(){
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
