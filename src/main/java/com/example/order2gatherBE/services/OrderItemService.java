package com.example.order2gatherBE.services;

import com.example.order2gatherBE.exceptions.EntityNotFoundException;
import com.example.order2gatherBE.models.OrderEventModel;
import com.example.order2gatherBE.repository.OrderEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.order2gatherBE.models.OrderItemModel;
import com.example.order2gatherBE.repository.OrderItemRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderEventRepository orderEventRepository;

    // init order event member
    public void initOrderEventMembers(int uid, int oid){
        orderItemRepository.initOrderEventMembers(uid,oid);
    }
    // get host id
    public int getHostID(int oid){
        return orderItemRepository.getHost(oid);
    }

    // get existing user in an order event
    public List<Integer> getUsers(int oid){
        return orderItemRepository.getUsers(oid);
    }

    // to add item into DB
    public void addOrderItem(OrderItemModel orderItemModel){
        orderItemRepository.addOrderItem(orderItemModel);
    }

    // to modify/update item in DB
    public void hostModifyOrderItem(OrderItemModel orderItemModel){
        orderItemRepository.hostModifyOrderItem(orderItemModel);
    }
    public void userModifyOrderItem(OrderItemModel orderItemModel){
        orderItemRepository.userModifyOrderItem(orderItemModel);
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
    public Map<String, Object> organizeHostViewOrders(int oid) {
        // check whether order event exist
        OrderEventModel existingOrderEvent = orderEventRepository.getOrderEventByOid(oid);
        if (existingOrderEvent == null) {
            throw new EntityNotFoundException("OrderEvent not found with oid=" + oid);
        }

        List<OrderItemModel> orderItems = orderItemRepository.getHostViewOrderItems(oid);
        Map<Integer, List<OrderItemModel>> userOrdersMap = new HashMap<>();
        int totalPrice = 0;

        for (OrderItemModel orderItem : orderItems) {
            int uid = orderItem.getUID();
            totalPrice += orderItem.getHostViewPrice() * orderItem.getNum();

            userOrdersMap.computeIfAbsent(uid, k -> new ArrayList<>()).add(orderItem);
        }

        List<Map<String, Object>> ordersList = new ArrayList<>();
        for (Map.Entry<Integer, List<OrderItemModel>> entry : userOrdersMap.entrySet()) {
            int uid = entry.getKey();
            List<OrderItemModel> userOrders = entry.getValue();

            String username = userOrders.stream()
                    .map(OrderItemModel::getUserName)
                    .findFirst()
                    .orElse(null);

            // Calculate total price for the user
            int userTotalPrice = userOrders.stream()
                    .mapToInt(orderItem -> orderItem.getHostViewPrice() * orderItem.getNum())
                    .sum();

            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("uid", uid);
            orderMap.put("username", username);

            List<Map<String, Object>> foodList = userOrders.stream()
                    .map(OrderItemModel::mapHostViewOrderItem)
                    .collect(Collectors.toList());

            orderMap.put("food", foodList);
            orderMap.put("userTotalPrice", userTotalPrice);
            ordersList.add(orderMap);
        }

        Map<String, Object> responseData = Map.of("oid", oid, "orders", ordersList, "totalPrice", totalPrice);
        return responseData;
    }
}
