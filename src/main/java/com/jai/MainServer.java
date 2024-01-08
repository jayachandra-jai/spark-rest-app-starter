package com.jai;

import com.jai.controllers.AuthController;
import com.jai.controllers.InternalController;
import com.jai.controllers.MainController;
import com.jai.controllers.UserController;
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
//        enableCORS("*", "*", "*");
        before((req, res) -> {
            if (!AuthHelper.isAuthenticated(req,res,contextPath)) {
                log.info("req:"+req);
                halt(401, "Unauthorized");
            }

        });
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
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
        new UserController(contextPath);
    }
    private static void enableCORS(final String origin, final String methods, final String headers) {
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: Allow credentials if needed (e.g., for cookies)
            // response.header("Access-Control-Allow-Credentials", "true");
        });

        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });
    }
}