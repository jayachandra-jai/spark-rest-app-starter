# spark-rest-app-starter
Simple Rest application starter for micro-services using java spark framework.
  ### Technologies/concepts Used
      * Java 8
      * Maven 3
      * Spark 2.9.4
      * JWT Authentication with access and refresh token logic
      * Authentictaion with IP whitelisting
      * User role management
      * lombok 1.18.28
      * Logging Enabled with Slf4j
 ## Steps to Setup

**1. Clone the application**

```bash
https://github.com/jayachandra-jai/spark-rest-app-starter.git
```

**2. Build and run the app using maven**

```bash
mvn clean insatll
mvn package
java -cp target/SparkRestAppStarter-1.0.0.jar:target/lib/* com.jai.MainServer <PORT>

```

**3. Configuration & Testing the application**
+ add the respective auth and jwt configs in config/config.properties
+ Add the users with their roles in UserService or you can implement db for user service
+ Get the access & refresh token with get token api `curl -X POST http://localhost:<PORT>/<context_path>/auth/getToken -H 'content-type: application/json' -d '{"userName":"endUser","password":"test123"}'`
+ if access token is expired you can new token with refresh token `curl -X POST 'http://localhost:<PORT>/<context_path>/auth/refreshToken?refresh_token=<resfresh_token>'`
+ You can access any api by passing access token in header `curl -X GET http://localhost:<PORT>/myapp/internal/test -H 'authorization:<access_token>'` 

    
      

