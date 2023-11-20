package com.example.order2gatherBE.controllers;

import com.example.order2gatherBE.models.OrderEventModel;
import com.example.order2gatherBE.services.OrderEventService;
import com.example.order2gatherBE.exceptions.RequestBodyValidationException;
import com.example.order2gatherBE.util.SecretCodeGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequestMapping(value="/orderEvent", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class OrderEventController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    OrderEventService orderEventService;
    @PostMapping("/create")
    public ResponseEntity<String> createOrderEvent(@Valid @RequestBody OrderEventModel orderEventModel) {
        // check whether required data is filled
        if (orderEventModel.getRId() == null || orderEventModel.getHostID() == null) {
            logger.error("Invalid OrderEventModel: rid and hostID cannot be null. rid={}, hostID={} is provided.", orderEventModel.getRId(), orderEventModel.getHostID());
            throw new RequestBodyValidationException("\'rid\' and \'hostID\' are required and the value should be valid.");
        }
        // check whether the rid is valid

        // generate 6-digit secret code
        String secretCode = SecretCodeGenerator.generateSecretCode(orderEventModel.getHostID());
        orderEventModel.setSecretCode(secretCode);

        orderEventService.createOrderEvent(orderEventModel);

        String jsonResponse = String.format("{\"Secret Code\": \"%s\"}", secretCode);
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @ExceptionHandler(RequestBodyValidationException.class)
    public ResponseEntity<String> handleValidationException(RequestBodyValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
