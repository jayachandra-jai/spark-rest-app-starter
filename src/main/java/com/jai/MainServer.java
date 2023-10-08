package com.jai;

import com.jai.controllers.AuthController;
import com.jai.controllers.InternalController;
import com.jai.controllers.MainController;
import com.jai.services.AuthHelper;
import com.jai.utils.ConfigHelper;
import lombok.extern.slf4j.Slf4j;

import static spark.Spark.*;

@Slf4j
public class MainServer {
    public static void main(String[] args) {
        String port = args[0];
        log.info("Starting server on port: " + port);
        port(Integer.parseInt(port));
        String contextPath= ConfigHelper.getSpecificValue("application_context","/myapp");
        before((req, res) -> {
            if (!AuthHelper.isAuthenticated(req,contextPath)) {
                log.info("req:"+req);
                halt(401, "Unauthorized");
            }
        });
        after((req, res) -> {
            //add response headers if any
            res.header("Developer", "Jai");
        });
        get(contextPath+"/test", (req, res) -> "SUCCESS");

        //initialize all controllers
        new MainController(contextPath);
        new AuthController(contextPath);
        new InternalController(contextPath);
    }
}