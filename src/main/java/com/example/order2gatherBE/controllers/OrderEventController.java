package com.example.order2gatherBE.controllers;

import com.example.order2gatherBE.models.OrderEventModel;
import com.example.order2gatherBE.services.OrderEventService;
import com.example.order2gatherBE.exceptions.RequestBodyValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/orderEvent", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class OrderEventController {
    @Autowired
    OrderEventService orderEventService;

    @PostMapping("/create")
    public ResponseEntity<String> createOrderEvent(@Valid @RequestBody OrderEventModel orderEventModel) {
        orderEventService.createOrderEvent(orderEventModel);

        String jsonResponse = String.format("{\"id\": %d, \"Secret Code\": \"%s\"}", orderEventModel.getId(), orderEventModel.getSecretCode());
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @ExceptionHandler(RequestBodyValidationException.class)
    public ResponseEntity<String> handleValidationException(RequestBodyValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
