package com.jai.controllers;

import com.google.gson.Gson;
import com.jai.model.auth.UserPrincipal;
import com.jai.model.modules.Item;
import com.jai.model.modules.User;
import com.jai.model.response.GenericResponse;
import com.jai.model.response.ResponseCode;
import com.jai.services.AuthHelper;
import com.jai.services.ItemService;
import com.jai.services.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import static spark.Spark.*;

/**
 * @author jai
 * created on 07/09/23
 */
@Slf4j
public class UserController {
    public UserController(String contextPath){
        Gson gson=new Gson();
        path(contextPath+"/user", () -> {

            get("/getUser", (req, res) -> {
                res.header("Content-Type", "application/json");
                UserPrincipal userPrincipal = req.attribute("userPrincipal");
                log.info("Entering get User:{}, userPrincipal:{}", req.body(), userPrincipal);

                List<String> rolesAllowed = Arrays.asList("admin,ops_user,end_user".split(","));
                List<String> authAllowed = Arrays.asList("TOKEN".split(","));
                if (!AuthHelper.isAllowed(userPrincipal, rolesAllowed, authAllowed)) {
                    res.header("Content-Type", "text/html");
                    halt(403, "Access Denied");
                }

                GenericResponse<User> response = new GenericResponse();
                try {
                    User user= UserService.getUserProfile(userPrincipal.getUser());
                    response.setStatus(ResponseCode.SUCCESS);
                    response.setResponseDesc("User fetched Successfully");
                    response.setResponseObj(user);
                } catch (Exception e) {
                    log.error("Error add User", e);
                    response.setStatus(ResponseCode.EXCEPTION);
                    response.setResponseDesc(e.getMessage());
                }
                log.info("Exiting get User:" + response);
                return gson.toJson(response);
            });
        });
    }
}
