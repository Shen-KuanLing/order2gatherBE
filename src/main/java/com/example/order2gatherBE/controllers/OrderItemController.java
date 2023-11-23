package com.example.order2gatherBE.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.order2gatherBE.models.OrderItemModel;
import com.example.order2gatherBE.services.OrderItemService;

import java.util.List;

@Controller
public class OrderItemController {
    @Autowired
    OrderItemService orderItemService;
    
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
    
    // get all order item in an order event  
    @PutMapping("/ordering/getAll")
    public List<OrderItemModel> getAllUserOrderItem(int uid,int oid){
        return orderItemService.getAllOrderItem(uid, oid);

    }
    
    // mark an item deleted in DB
    @PutMapping("/ordering/delete")
    public String deleteUserOderItem(@RequestBody OrderItemModel formData){
        orderItemService.deleteOrderItem(formData);
        return "deleted";
    }
}
