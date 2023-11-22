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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/view")
    public ResponseEntity<?> getOrderEvent(@RequestParam(required = false) Integer oid,
                                           @RequestParam(required = false) Integer uid) {
        if (oid != null && uid == null) {
            OrderEventModel orderEventModel = orderEventService.getOrderEventByOid(oid);
            if (orderEventModel != null) {
                return new ResponseEntity<>(orderEventModel, HttpStatus.OK);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "There's no corresponding order event.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } else if (uid != null && oid == null) {
            List<OrderEventModel> orderEventModels = orderEventService.getOrderEventByUid(uid);
            if (!orderEventModels.isEmpty()) {
                return new ResponseEntity<>(orderEventModels, HttpStatus.OK);
            } else {
                String message = String.format("User %d doesn't join any order event so far.", uid);
                Map<String, String> response = new HashMap<>();
                response.put("message", message);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } else {
            // Handle the case where neither oid nor uid is provided
            Map<String, String> response = new HashMap<>();
            response.put("message", "Please provide either oid or uid parameter.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(RequestBodyValidationException.class)
    public ResponseEntity<String> handleValidationException(RequestBodyValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
