package com.example.order2gatherBE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.order2gatherBE.models.OrderItemModel;
import com.example.order2gatherBE.services.OrderItemService;

import java.util.*;

@Controller
public class OrderItemController {
    @Autowired
    OrderItemService orderItemService;

    // init order event member tracking (one user at a time)
    @PostMapping("/ordering/init")
    public String initOrderEventMembers(@RequestParam int uid,@RequestParam int oid ){
        orderItemService.initOrderEventMembers(uid,oid);
        return ("uid :" + Integer.toString(uid) + " added");
    }

    // add new item to DB
    @PostMapping("/ordering/add")
    public String addUserOrderItem(@RequestBody OrderItemModel formData){
        orderItemService.addOrderItem(formData);
        return "added";
    }

    // get all existing users in and order event
    @GetMapping("/ordering/getUsers")
    public List<Integer> getUsers(@RequestParam int oid ){
        return orderItemService.getUsers(oid);
    }

    // get all order item in an order event "of a specific user"
    @GetMapping("/ordering/getUserOrders")
    public List<OrderItemModel> getUserOrderItem(@RequestParam int uid, @RequestParam int oid){
        return orderItemService.getUserOrderItem(uid, oid);
    }

    // get all order item in an order event
    @GetMapping("/ordering/getAllOrders")
    public List<OrderItemModel> getAllOrderItem( @RequestParam int oid){
        return orderItemService.getAllOrderItem(oid);
    }

    // overwrite the data in DB
    @PutMapping("/ordering/modify")
    public String modifyUserOderItem(@RequestBody OrderItemModel formData){
        orderItemService.modifyOrderItem(formData);
        return "modified";
    }

    // mark an item deleted in DB
    @DeleteMapping("/ordering/delete")
    public String deleteUserOderItem(@RequestBody OrderItemModel formData){
        orderItemService.deleteOrderItem(formData);
        return "deleted";
    }
}
