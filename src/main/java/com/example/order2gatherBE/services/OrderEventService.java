package com.example.order2gatherBE.services;

import com.example.order2gatherBE.exceptions.RequestBodyValidationException;
import com.example.order2gatherBE.models.OrderEventModel;
import com.example.order2gatherBE.repository.OrderEventRepository;
import com.example.order2gatherBE.util.SecretCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderEventService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    OrderEventRepository orderEventRepository;

    public void createOrderEvent(OrderEventModel orderEventModel) {
        validateOrderEventModel(orderEventModel);
        generateAndSetSecretCode(orderEventModel);
        orderEventRepository.createOrderEvent(orderEventModel);
    }
    public OrderEventModel getOrderEventByOid(Integer oid) {
        return orderEventRepository.getOrderEventByOid(oid);
    }
    public List<OrderEventModel> getOrderEventByUid(Integer uid) {
        return orderEventRepository.getOrderEventByUid(uid);
    }

    private void validateOrderEventModel(OrderEventModel orderEventModel) {
        if (orderEventModel.getRId() == null || orderEventModel.getHostID() == null) {
            logger.error("Invalid OrderEventModel: rid and hostID cannot be null. rid={}, hostID={} is provided.", orderEventModel.getRId(), orderEventModel.getHostID());
            throw new RequestBodyValidationException("'rid' and 'hostID' are required and the value should be valid.");
        }
    }

    private void generateAndSetSecretCode(OrderEventModel orderEventModel) {
        String secretCode = SecretCodeGenerator.generateSecretCode(orderEventModel.getHostID());
        orderEventModel.setSecretCode(secretCode);
    }
}
