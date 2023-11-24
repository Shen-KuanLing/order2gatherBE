package com.example.order2gatherBE.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.order2gatherBE.models.OrderItemModel;
import com.example.order2gatherBE.repository.OrderItemRepository;

import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    OrderItemRepository orderItemRepository;

    public void initOrderEventMembers(int uid, int oid){
        orderItemRepository.initOrderEventMembers(uid,oid);
    }

    // to add item into DB
    public void addOrderItem(OrderItemModel orderItemModel){
        orderItemRepository.addOrderItem(orderItemModel);
    }

    // to modify/update item in DB 
    public void modifyOrderItem(OrderItemModel orderItemModel){
        orderItemRepository.modifyOrderItem(orderItemModel);
    }
    
    // to mark an item deleted in DB
    public void deleteOrderItem(OrderItemModel orderItemModel){
        orderItemRepository.deleteOrderItem(orderItemModel);
    }

    // to get all item of a user in an order event
    public List<OrderItemModel> getUserOrderItem(int uid, int oid){
        return orderItemRepository.getUserOrderItem(uid, oid);
    }

    
    // to get all item of an order event
    public List<OrderItemModel> getAllOrderItem(int oid){
        return orderItemRepository.getAllOrderItem(oid);
    }
}
