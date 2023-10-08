package com.jai.model.modules;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jai
 * created on 24/09/23
 */
@Data
public class User {
    String userName,password,role;

    public User(String userName, String password,String role) {
        this.userName = userName;
        this.password = password;
        this.role=role;
    }
}
