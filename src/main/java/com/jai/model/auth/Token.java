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
    private String userName;
    private Date refreshTokenExpiredAt;
    private Date refreshTokenIssuedAt;
    private Date accessTokenExpiredAt;
    private Date accessTokenIssuedAt;

}
