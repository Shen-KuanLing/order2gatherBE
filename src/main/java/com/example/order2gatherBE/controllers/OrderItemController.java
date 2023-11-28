package com.example.order2gatherBE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.order2gatherBE.models.OrderItemModel;
import com.example.order2gatherBE.services.AuthenticationService;
import com.example.order2gatherBE.services.OrderItemService;

import java.util.*;

@Controller
public class OrderItemController {
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    private AuthenticationService authenticationService;

    // init order event member tracking (one user at a time)
    // @PostMapping("/ordering/init")
    // public ResponseEntity<String> initOrderEventMembers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestParam(value="uid",required = true) int uid, @RequestParam(value="oid",required = true) int oid ){
    //     token = token.replace("Bearer ", "");
    //     int auth_uid = authenticationService.verify(token);
    //     if(auth_uid == -1) {
    //         return new ResponseEntity<>("message User doesn't have the permission.", HttpStatus.FORBIDDEN);
    //     }
    //     orderItemService.initOrderEventMembers(uid,oid);
    //     return new ResponseEntity<>(("uid :" + Integer.toString(uid) + " added"), HttpStatus.OK);
    // }

    // add new item to DB
    @PostMapping("/ordering/add")
    public ResponseEntity<String> addUserOrderItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestBody OrderItemModel formData){
        token = token.replace("Bearer ", "");
        int auth_uid = authenticationService.verify(token);
        if(auth_uid == -1) {
            return new ResponseEntity<>("message User doesn't have the permission.", HttpStatus.FORBIDDEN);
        }
        orderItemService.addOrderItem(formData);
        return new ResponseEntity<>(("food :" + formData.getFoodName() + " added"), HttpStatus.OK);
    }

    // get all existing users in and order event
    @GetMapping("/ordering/getUsers")
    public ResponseEntity<List<Integer>> getUsers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestParam int oid ){
        token = token.replace("Bearer ", "");
        int auth_uid = authenticationService.verify(token);
        if(auth_uid == -1) {
            return null;
        }
        return new ResponseEntity<>(orderItemService.getUsers(oid),HttpStatus.OK);
    }

    // get all order item in an order event "of a specific user"
    @GetMapping("/ordering/getUserOrders")
    public ResponseEntity<List<OrderItemModel>> getUserOrderItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@RequestParam int uid, @RequestParam int oid){
        token = token.replace("Bearer ", "");
        int auth_uid = authenticationService.verify(token);
        if(auth_uid == -1) {
            return null;
        }
        return new ResponseEntity<>(orderItemService.getUserOrderItem(uid, oid),HttpStatus.OK);
    }

    // get all order item in an order event
    @GetMapping("/ordering/getAllOrders")
    public ResponseEntity<List<OrderItemModel>> getAllOrderItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam int oid){
        token = token.replace("Bearer ", "");
        int auth_uid = authenticationService.verify(token);
        if(auth_uid == -1) {
            return null;
        }
        return new ResponseEntity<>(orderItemService.getAllOrderItem(oid),HttpStatus.OK);
    }

    // overwrite the data in DB:
    // two condition
        // modifier is host
        // modifier is not host
    @PutMapping("/ordering/modify")
    public ResponseEntity<String> modifyUserOderItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody OrderItemModel formData){
        token = token.replace("Bearer ", "");
        int auth_uid = authenticationService.verify(token);
        if(auth_uid == -1) {
            return new ResponseEntity<>("message User doesn't have the permission.", HttpStatus.FORBIDDEN);
        }
        orderItemService.modifyOrderItem(formData);
        int hid=orderItemService.getHostID(formData.getOID());
        if(hid==formData.getUID()) return new ResponseEntity<>(("Host: fid-" + formData.getFID() + " modified"), HttpStatus.OK);
        else return new ResponseEntity<>(("Orderer: fid-" + formData.getFID() + " modified"), HttpStatus.OK);
    }

    // mark an item deleted in DB
    @DeleteMapping("/ordering/delete")
    public ResponseEntity<String> deleteUserOderItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody OrderItemModel formData){
        token = token.replace("Bearer ", "");
        int auth_uid = authenticationService.verify(token);
        if(auth_uid == -1) {
            return new ResponseEntity<>("message User doesn't have the permission.", HttpStatus.FORBIDDEN);
        }
        orderItemService.deleteOrderItem(formData);
        return new ResponseEntity<>(("food :" + formData.getFoodName() + " delete"), HttpStatus.OK);
    }
}
