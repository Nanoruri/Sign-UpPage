package me.jh.core.dto.token;

import me.jh.core.dto.token.builder.JWTokenBuilder;

public class JWToken {

    private String grantType;
    private String accessToken;
    private String refreshToken;

    public JWToken(String grantType, String accessToken, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // Getters and setters

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static JWTokenBuilder builder() {
        return new JWTokenBuilder();
    }
}
