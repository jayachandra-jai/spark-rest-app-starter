package com.jai.services;

import com.jai.model.auth.Token;
import com.jai.model.auth.UserPrincipal;
import com.jai.utils.ConfigHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import spark.Request;

import java.util.Arrays;
import java.util.List;

/**
 * @author jai
 * created on 07/09/23
 */
@Slf4j
public class AuthHelper {
    public static boolean isAuthenticated(Request request,String contextPath){
        String ipAddress=request.ip();
        String path=request.pathInfo();
        String is_auth=ConfigHelper.getSpecificValue("is_auth_enabled","true");
        log.info("Accessing url: {} from IP:{} auth:{}",path,ipAddress,is_auth);
        if(BooleanUtils.toBoolean(is_auth)){
            //check non auth urls
            List<String> non_auth_urls=Arrays.asList(ConfigHelper.getSpecificValue("non_auth_urls","auth/").split(","));
            for (String url:non_auth_urls) {
                if(path.startsWith(contextPath+"/"+url)){
                    request.attribute("userPrincipal",new UserPrincipal(null,"NON_AUTH",null));
                    return true;
                }
            }

            List<String> whitelisted_ip_auth_urls=Arrays.asList(ConfigHelper.getSpecificValue("whitelisted_ip_auth_urls","internal/").split(","));
            for (String url:whitelisted_ip_auth_urls) {
                if(path.startsWith(contextPath+"/"+url)){
                    log.info("Internal url:"+contextPath+"/"+url);
                    List<String> whitelisted_ips= Arrays.asList(ConfigHelper.getSpecificValue("whitelisted_ips","").split(","));
                    log.info("IP is not whitelisted ips:{}",whitelisted_ips);
                    if(whitelisted_ips.contains(ipAddress)){
                        log.info("IP is not whitelisted:{}",ipAddress);
                        request.attribute("auth_type","ip_white_listed");
                        request.attribute("userPrincipal",new UserPrincipal(null,"WHITE_LISTED_IP",null));
                        return true;
                    }
                }
            }

            String tokenStr = request.headers("Authorization");
            if (tokenStr != null ) {
                UserPrincipal userPrincipal=TokenHelper.validateToken(tokenStr);
                log.info("Inside Token auth:"+tokenStr+" user:"+userPrincipal);
                if(userPrincipal!=null && StringUtils.isNotEmpty(userPrincipal.getUser())){
                    request.attribute("userPrincipal",userPrincipal);
                    return true;
                }
            }

        }
        return false;
    }

    public static boolean isAllowed(UserPrincipal userPrincipal, List<String> rolesAllowed, List<String> authAllowed) {
        log.debug("Inside isAllowed userPrincipal:{} rolesAllowed: {} authAllowed: {}",userPrincipal,rolesAllowed,authAllowed);
        if((rolesAllowed.contains(userPrincipal.getRole()) || rolesAllowed.contains("ALL")) && (authAllowed.contains(userPrincipal.getAuthType()) || authAllowed.contains("ALL")))
            return true;
        return false;
    }
}
