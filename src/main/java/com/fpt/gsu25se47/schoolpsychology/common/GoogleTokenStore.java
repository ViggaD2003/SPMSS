package com.fpt.gsu25se47.schoolpsychology.common;

import org.springframework.stereotype.Component;

@Component
public class GoogleTokenStore {

    private String accessToken;
    private String refreshToken;

    public void saveTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void updateAccessToken(String token) {
        this.accessToken = token;
    }
}

