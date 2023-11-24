package com.example.order2gatherBE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.order2gatherBE.models.OrderItemModel;
import com.example.order2gatherBE.services.OrderItemService;

import java.util.List;

@Controller
public class OrderItemController {
    @Autowired
    OrderItemService orderItemService;
    
    // init: member tracking
    @PutMapping("/ordering/init")
    public String initOrderEventMembers(@RequestParam int uid,@RequestParam int oid ){
        orderItemService.initOrderEventMembers(uid,oid);
        return "added";
    }



    // add new item to DB
    @PutMapping("/ordering/add")
    public String addUserOrderItem(@RequestBody OrderItemModel formData){
        orderItemService.addOrderItem(formData);
        return "added";
    }

    // overwrite the data in DB
    @PutMapping("/ordering/modify")
    public String modifyUserOderItem(@RequestBody OrderItemModel formData){
        orderItemService.modifyOrderItem(formData);
        return "modified";
    }
    
    // get all order item in an order event "of a specific user"
    @PutMapping("/ordering/getUserOrders")
    public List<OrderItemModel> getUserOrderItem(@RequestParam int uid, @RequestParam int oid){
        return orderItemService.getUserOrderItem(uid, oid);
    }
    
    // get all order item in an order event
    @PutMapping("/ordering/getAllOrders")
    public List<OrderItemModel> getAllOrderItem( @RequestParam int oid){
        return orderItemService.getAllOrderItem(oid);
    }
    
    // mark an item deleted in DB
    @PutMapping("/ordering/delete")
    public String deleteUserOderItem(@RequestBody OrderItemModel formData){
        orderItemService.deleteOrderItem(formData);
        return "deleted";
    }
}
