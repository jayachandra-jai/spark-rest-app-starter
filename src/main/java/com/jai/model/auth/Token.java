package com.jai.model.auth;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author jai
 * created on 24/09/23
 */
@Data
@Builder
public class Token {
    private String accessToken;
    private String refreshToken;
    private String role;
    private Date refreshTokenExpiredAt;
    private Date refreshTokenIssuedAt;
    private Date accessTokenExpiredAt;
    private Date accessTokenIssuedAt;

    public Token() {
    }

    public Token(String accessToken, String refreshToken, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role=role;
    }

    public Token(String accessToken, String refreshToken, String role, Date refreshTokenExpiredAt, Date refreshTokenIssuedAt, Date accessTokenExpiredAt, Date accessTokenIssuedAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
        this.refreshTokenExpiredAt = refreshTokenExpiredAt;
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
        this.accessTokenExpiredAt = accessTokenExpiredAt;
        this.accessTokenIssuedAt = accessTokenIssuedAt;
    }
}
