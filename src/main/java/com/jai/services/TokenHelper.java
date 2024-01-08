package com.jai.services;

import com.google.gson.Gson;
import com.jai.model.auth.UserPrincipal;
import com.jai.utils.ConfigHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jai
 * created on 24/09/23
 */
@Slf4j
public class TokenHelper {
    public static final String ACCESS_TOKEN_SECRET_KEY = ConfigHelper.getSpecificValue("JWT_ACCESS_AUTH_SECRET_KEY","33hgasuysa87sahsa87shas78");
    public static final String REFRESH_TOKEN_SECRET_KEY = ConfigHelper.getSpecificValue("JWT_REFRESH_AUTH_SECRET_KEY","7sahsa8kskkksayt2hjwqyueeq");
    public static final long ACCESS_TOKEN_EXPIRATION_TIME = Long.parseLong(ConfigHelper.getSpecificValue("JWT_ACCESS_TOKEN_VALIDITY_IN_MIN","15")) * 60 * 1000; // 15 minutes
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = Long.parseLong(ConfigHelper.getSpecificValue("JWT_REFRESH_TOKEN_VALIDITY_IN_HOURS","24"))* 60 * 60 * 1000; // 7 days

    public static String generateAccessToken(String username,String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username",username);
        claims.put("role",role);
        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, ACCESS_TOKEN_SECRET_KEY)
                .compact();
    }

    public static String generateRefreshToken(String username,String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username",username);
        claims.put("role",role);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, REFRESH_TOKEN_SECRET_KEY)
                .compact();
    }
    public static UserPrincipal validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(ACCESS_TOKEN_SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            // You can perform additional validation here if needed
            String role=(String) claims.get("role");
            String userName=(String) claims.get("username");

            return new UserPrincipal(userName,"TOKEN",role);
        } catch (JwtException e) {
            log.error("Error in validateToken",e);
            return null;
        }
    }
}
