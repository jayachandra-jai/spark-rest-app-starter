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
        users.add(new User("jai","test123","admin","https://cdn.pixabay.com/photo/2014/04/03/10/32/businessman-310819_1280.png"));
        users.add(new User("endUser","test123","end_user","https://cdn.pixabay.com/photo/2016/11/18/23/38/child-1837375_1280.png"));
        users.add(new User("opsUser","test123","ops_user","https://cdn.pixabay.com/photo/2014/04/03/10/32/user-310807_1280.png"));
    }

    public static User getUser(Login  login){
        return users.stream()
                .filter(u -> u.getUserName().equals(login.getUserName()) && u.getPassword().equals(login.getPassword()))
                .findFirst()
                .orElse(null);
    }

    public static User getUserProfile(String userName){
        return users.stream()
                .filter(u -> u.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }
}
