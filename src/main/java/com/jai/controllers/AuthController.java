package com.jai.controllers;

import com.google.gson.Gson;
import com.jai.model.auth.Login;
import com.jai.model.auth.Token;
import com.jai.model.modules.User;
import com.jai.services.TokenHelper;
import com.jai.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

import static spark.Spark.path;
import static spark.Spark.post;

/**
 * @author jai
 * created on 24/09/23
 */
@Slf4j
public class AuthController {
    public AuthController(String contextPath){
        Gson gson=new Gson();
        path(contextPath+"/auth", () -> {

            post("/getToken", (req, res) -> {
                res.header("Content-Type","application/json");
                Login login=gson.fromJson(req.body(),Login.class);
                log.info("Inside Login :"+req);
                // Validate user credentials (in-memory user database)
                User user = UserService.getUser(login);
                if (user != null) {
                    String accessToken = TokenHelper.generateAccessToken(login.getUserName(),user.getRole());
                    String refreshToken = TokenHelper.generateRefreshToken(login.getUserName(),user.getRole());
                    Token token= Token.builder()
                            .accessToken(accessToken)
                            .accessTokenIssuedAt(new Date())
                            .accessTokenExpiredAt(new Date(System.currentTimeMillis()+TokenHelper.ACCESS_TOKEN_EXPIRATION_TIME))
                            .refreshToken(refreshToken)
                            .refreshTokenIssuedAt(new Date())
                            .refreshTokenExpiredAt(new Date(System.currentTimeMillis()+TokenHelper.REFRESH_TOKEN_EXPIRATION_TIME))
                            .role(user.getRole())
                            .userName(user.getUserName())
                            .build();
                    return token;
                } else {
                    res.status(401); // Unauthorized
                    return "Invalid credentials";
                }
            }, gson::toJson);

            post("/refreshToken", (req, res) -> {
                res.header("Content-Type","application/json");
                String refreshToken = req.queryParams("refresh_token");
                if(StringUtils.isNotEmpty(refreshToken)){
                    try {
                        Claims claims = Jwts.parser()
                                .setSigningKey(TokenHelper.REFRESH_TOKEN_SECRET_KEY)
                                .parseClaimsJws(refreshToken)
                                .getBody();
                        String role=(String) claims.get("role");
                        String userName=(String) claims.get("username");
                        log.info("Info is:"+claims.getExpiration());


                        String newAccessToken = TokenHelper.generateAccessToken(userName,role);
                        Token token= Token.builder()
                                .accessToken(newAccessToken)
                                .accessTokenIssuedAt(new Date())
                                .accessTokenExpiredAt(new Date(System.currentTimeMillis()+TokenHelper.ACCESS_TOKEN_EXPIRATION_TIME))
                                .refreshToken(refreshToken)
                                .refreshTokenIssuedAt(claims.getIssuedAt())
                                .refreshTokenExpiredAt(claims.getExpiration())
                                .role(role)
                                .build();
                        return token;
                    } catch (Exception e) {
                        res.status(401); // Unauthorized
                        return "Token expired";
                    }
                }else {
                    return "Invalid Token";
                }
            }, gson::toJson);

        });
    }
}
