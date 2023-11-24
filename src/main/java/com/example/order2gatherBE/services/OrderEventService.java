package com.example.order2gatherBE.services;

import com.example.order2gatherBE.exceptions.EntityNotFoundException;
import com.example.order2gatherBE.exceptions.ForbiddenException;
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
    public void updateOrderEvent(Integer oid, Integer uid, OrderEventModel updateRequest) {
        OrderEventModel existingOrderEvent = orderEventRepository.getOrderEventByOid(oid);

        if (existingOrderEvent == null) {
            throw new EntityNotFoundException("OrderEvent not found with oid=" + oid);
        }

        validateUserPermission(uid, existingOrderEvent.getHostID());

        // Update the fields based on the request body
        updateOrderEventFields(existingOrderEvent, updateRequest);

        orderEventRepository.updateOrderEvent(existingOrderEvent);
    }

    private void validateUserPermission(Integer uid, Integer hostId) {
        if (!uid.equals(hostId)) {
            throw new ForbiddenException("User does not have permission to update this OrderEvent.");
        }
    }

    private void updateOrderEventFields(OrderEventModel orderEvent, OrderEventModel updateOrderEvent) {
        // Update fields based on the request body
        if (updateOrderEvent.getStopOrderingTime() != null) {
            orderEvent.setStopOrderingTime(updateOrderEvent.getStopOrderingTime());
        }

        if (updateOrderEvent.getEstimatedArrivalTime() != null) {
            orderEvent.setEstimatedArrivalTime(updateOrderEvent.getEstimatedArrivalTime());
        }

        if (updateOrderEvent.getEndEventTime() != null) {
            orderEvent.setEndEventTime(updateOrderEvent.getEndEventTime());
        }

        if (updateOrderEvent.getStatus() != null) {
            // check whether status is valid
            int updatedStatus = updateOrderEvent.getStatus();
            if (updatedStatus >= 1 && updatedStatus <= 4) {
                orderEvent.setStatus(updatedStatus);
            } else {
                throw new IllegalArgumentException("Invalid status value. It should be between 1 and 4.");
            }
            orderEvent.setStatus(updateOrderEvent.getStatus());
        }
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
