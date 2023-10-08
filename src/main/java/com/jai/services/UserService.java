package com.jai.services;

import com.jai.model.auth.Login;
import com.jai.model.modules.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jai
 * created on 24/09/23
 */
public class UserService {
    public static List<User> users=new ArrayList<>();
    static {
        users.add(new User("jai","test123","admin"));
        users.add(new User("endUser","test123","end_user"));
        users.add(new User("opsUser","test123","ops_user"));
    }

    public static User getUser(Login  login){
        return users.stream()
                .filter(u -> u.getUserName().equals(login.getUserName()) && u.getPassword().equals(login.getPassword()))
                .findFirst()
                .orElse(null);
    }
}
