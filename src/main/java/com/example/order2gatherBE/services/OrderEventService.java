package com.example.order2gatherBE.services;

import com.example.order2gatherBE.models.OrderEventModel;
import com.example.order2gatherBE.repository.OrderEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderEventService {
    @Autowired
    OrderEventRepository orderEventRepository;
    public void createOrderEvent(OrderEventModel orderEventModel) {
        orderEventRepository.createOrderEvent(orderEventModel);
    }
}
