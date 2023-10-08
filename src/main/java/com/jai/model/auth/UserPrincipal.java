package com.jai.model.auth;

import lombok.Data;

/**
 * @author jai
 * created on 02/10/23
 */
@Data
public class UserPrincipal {
    String user,authType,role;

    public UserPrincipal(String user, String authType, String role) {
        this.user = user;
        this.authType = authType;
        this.role = role;
    }
}
