package com.jai.controllers;

import com.google.gson.Gson;
import com.jai.model.auth.UserPrincipal;
import com.jai.model.modules.Item;
import com.jai.model.response.GenericResponse;
import com.jai.model.response.ResponseCode;
import com.jai.services.AuthHelper;
import com.jai.services.ItemService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spark.Spark.*;

/**
 * @author jai
 * created on 07/09/23
 */
@Slf4j
public class MainController {
    public MainController(String contextPath){
        Gson gson=new Gson();
        path(contextPath+"/item", () -> {

            post("/addItem", (req, res) -> {
                res.header("Content-Type","application/json");
                UserPrincipal userPrincipal=req.attribute("userPrincipal");
                log.info("Entering add Item:{}, userPrincipal:{}",req.body(),userPrincipal);

                List <String> rolesAllowed= Arrays.asList("admin,ops_user".split(","));
                List <String> authAllowed= Arrays.asList("TOKEN".split(","));
                if(!AuthHelper.isAllowed(userPrincipal,rolesAllowed,authAllowed)){
                    res.header("Content-Type","text/html");
                    halt(403,"Access Denied");
                }

                GenericResponse<Item> response=new GenericResponse();
                try {
                    Item item=gson.fromJson(req.body(),Item.class);
                    ItemService.addItem(item);
                    response.setStatus(ResponseCode.SUCCESS);
                    response.setResponseDesc("Item Added Successfully");
                    response.setResponseObj(item);
                }catch (Exception e){
                    log.error("Error add Item",e);
                    response.setStatus(ResponseCode.EXCEPTION);
                    response.setResponseDesc(e.getMessage());
                }
                log.info("Exiting add Item:"+response);
                return gson.toJson(response);
            });

            get("/getItem/:id", (req, res) -> {
                res.header("Content-Type","application/json");
                log.info("Entering get Item :{}, userPrincipal:",req.params("id"));

                UserPrincipal userPrincipal=req.attribute("userPrincipal");
                List <String> rolesAllowed= Arrays.asList("ALL".split(","));
                List <String> authAllowed= Arrays.asList("WHITE_LISTED_IP,TOKEN".split(","));
                if(!AuthHelper.isAllowed(userPrincipal,rolesAllowed,authAllowed)){
                    res.header("Content-Type","text/html");
                    halt(403,"Access Denied");
                }
                GenericResponse<Item> response=new GenericResponse();
                try {
                    int id=Integer.parseInt(req.params("id"));
                    Item item= ItemService.getItem(id);
                    if(item!=null){
                        response.setStatus(ResponseCode.SUCCESS);
                        response.setResponseDesc("Item Fetched Successfully");
                        response.setResponseObj(item);
                    }else {
                        response.setStatus(ResponseCode.FAILED);
                        response.setResponseDesc("Item not found");
                    }

                }catch (Exception e){
                    log.error("Error get Item",e);
                    response.setStatus(ResponseCode.EXCEPTION);
                    response.setResponseDesc(e.getMessage());
                }
                log.info("Exiting get Item:"+response);
                return gson.toJson(response);
            });
            put("/checkItem/:id", (req, res) -> {
                res.header("Content-Type","application/json");
                log.info("Entering get Item :{}, userPrincipal:",req.params("id"));

                UserPrincipal userPrincipal=req.attribute("userPrincipal");
                List <String> rolesAllowed= Arrays.asList("ALL".split(","));
                List <String> authAllowed= Arrays.asList("WHITE_LISTED_IP,TOKEN".split(","));
                if(!AuthHelper.isAllowed(userPrincipal,rolesAllowed,authAllowed)){
                    res.header("Content-Type","text/html");
                    halt(403,"Access Denied");
                }
                GenericResponse<Item> response=new GenericResponse();
                try {
                    int id=Integer.parseInt(req.params("id"));
                    if(ItemService.checkItem(id)){
                        response.setStatus(ResponseCode.SUCCESS);
                        response.setResponseDesc("Item updated Successfully");
                    }else {
                        response.setStatus(ResponseCode.FAILED);
                        response.setResponseDesc("Item not found");
                    }

                }catch (Exception e){
                    log.error("Error get Item",e);
                    response.setStatus(ResponseCode.EXCEPTION);
                    response.setResponseDesc(e.getMessage());
                }
                log.info("Exiting get Item:"+response);
                return gson.toJson(response);
            });

            get("/getAllItems", (req, res) -> {
                res.header("Content-Type","application/json");
                log.info("Entering get all items");
                UserPrincipal userPrincipal=req.attribute("userPrincipal");
                List <String> rolesAllowed= Arrays.asList("ALL".split(","));
                List <String> authAllowed= Arrays.asList("WHITE_LISTED_IP,TOKEN".split(","));
                if(!AuthHelper.isAllowed(userPrincipal,rolesAllowed,authAllowed)){
                    res.header("Content-Type","text/html");
                    halt(403,"Access Denied");
                }
                GenericResponse<List<Item>> response=new GenericResponse();
                try {
                    response.setStatus(ResponseCode.SUCCESS);
                    response.setResponseDesc("Items Fetched Successfully");
                    response.setResponseObj(ItemService.getAllItems());
                }catch (Exception e){
                    log.error("Error get Item",e);
                    response.setStatus(ResponseCode.EXCEPTION);
                    response.setResponseDesc(e.getMessage());
                }
                log.info("Exiting getAllItem"+response);

                return gson.toJson(response);
            });

            delete("/removeItem/:id", (req, res) -> {
                res.header("Content-Type","application/json");
                log.info("Entering remove item :{}",req.params("id"));
                List <String> rolesAllowed= Arrays.asList("ALL".split(","));
                List <String> authAllowed= Arrays.asList("WHITE_LISTED_IP,TOKEN".split(","));
                UserPrincipal userPrincipal=req.attribute("userPrincipal");
                if(!AuthHelper.isAllowed(userPrincipal,rolesAllowed,authAllowed)){
                    res.header("Content-Type","text/html");
                    halt(403,"Access Denied");
                }
                GenericResponse<Item> response=new GenericResponse();
                try {
                    int id=Integer.parseInt(req.params("id"));
                    Item item= ItemService.deleteItem(id);
                    if(item!=null){
                        response.setStatus(ResponseCode.SUCCESS);
                        response.setResponseDesc("Item Deleted Successfully");
                        response.setResponseObj(item);
                    }else {
                        response.setStatus(ResponseCode.FAILED);
                        response.setResponseDesc("Item not found");
                    }
                }catch (Exception e){
                    log.error("Error get Item",e);
                    response.setStatus(ResponseCode.EXCEPTION);
                    response.setResponseDesc(e.getMessage());
                }
                log.info("Exiting get Item:"+response);
                return gson.toJson(response);
            });

        });
    }
}
