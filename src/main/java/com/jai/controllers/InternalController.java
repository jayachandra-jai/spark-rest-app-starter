package com.jai.controllers;

import com.google.gson.Gson;
import com.jai.model.auth.UserPrincipal;
import com.jai.services.AuthHelper;

import java.util.Arrays;
import java.util.List;

import static spark.Spark.*;

/**
 * @author jai
 * created on 24/09/23
 */
public class InternalController {
    public InternalController(String contextPath) {
        Gson gson = new Gson();
        path(contextPath + "/internal", () -> {

            get("/test",(req,res)->{
                res.header("Content-Type","text/html");
                UserPrincipal userPrincipal=req.attribute("userPrincipal");
                List<String> rolesAllowed= Arrays.asList("ALL".split(","));
                List <String> authAllowed= Arrays.asList("WHITE_LISTED_IP,TOKEN".split(","));
                if(!AuthHelper.isAllowed(userPrincipal,rolesAllowed,authAllowed)){
                    res.header("Content-Type","text/html");
                    halt(403,"Access Denied");
                }
                return "SUCCESS";
            });
        });
    }
}
