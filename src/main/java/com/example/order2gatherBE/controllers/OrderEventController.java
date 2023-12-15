package com.example.order2gatherBE.controllers;

import com.example.order2gatherBE.exceptions.EntityNotFoundException;
import com.example.order2gatherBE.exceptions.ForbiddenException;
import com.example.order2gatherBE.exceptions.RequestBodyValidationException;
import com.example.order2gatherBE.models.OrderEventModel;
import com.example.order2gatherBE.services.AuthenticationService;
import com.example.order2gatherBE.services.OrderEventService;
import com.example.order2gatherBE.services.OrderItemService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    value = "/orderEvent",
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class OrderEventController {
    @Autowired
    OrderEventService orderEventService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    private AuthenticationService authenticationService;

    Map<String, String> response = new HashMap<>();
    Map<String, Integer> statusResponse = new HashMap<>();

    @PostMapping("/create")
    public ResponseEntity<?> createOrderEvent(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
        @Valid @RequestBody OrderEventModel orderEventModel
    ) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            response.put("message", "User doesn't have the permission.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        orderEventModel.setHostID(uid);
        int status = orderEventService.createOrderEvent(orderEventModel);

        String jsonResponse = String.format(
            "{\"id\": %d, \"SecretCode\": \"%s\"}",
            orderEventModel.getId(),
            orderEventModel.getSecretCode()
        );
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    @GetMapping("/view")
    public ResponseEntity<?> getOrderEvent(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
        @RequestParam(required = false) Integer oid
    ) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            response.put("message", "User doesn't have the permission.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        if (oid != null) {
            OrderEventModel orderEventModel = orderEventService.getOrderEventByOid(
                oid
            );
            if (orderEventModel != null) {
                return new ResponseEntity<>(orderEventModel, HttpStatus.OK);
            } else {
                response.put(
                    "message",
                    "There's no corresponding order event."
                );
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } else {
            List<OrderEventModel> orderEventModels = orderEventService.getOrderEventByUid(
                uid
            );
            if (!orderEventModels.isEmpty()) {
                return new ResponseEntity<>(orderEventModels, HttpStatus.OK);
            } else {
                String message = String.format(
                    "User %d doesn't join any order event so far.",
                    uid
                );
                response.put("message", message);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }
    }

    @PatchMapping("/update/{oid}")
    public ResponseEntity<?> updateOrderEvent(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
        @PathVariable("oid") Integer oid,
        @RequestBody OrderEventModel updatedOrderEvent
    ) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            response.put("message", "User doesn't have the permission.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        orderEventService.updateOrderEvent(oid, uid, updatedOrderEvent);
        return new ResponseEntity<>(
            "OrderEvent updated successfully.",
            HttpStatus.OK
        );
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinOrderEvent(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
        @RequestParam(required = true) String SecretCode
    ) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            response.put("message", "User doesn't have the permission.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        int status = orderEventService.joinOrderEvent(SecretCode, uid);
        statusResponse.put("status", status);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    @GetMapping("/organize")
    public ResponseEntity<?> organizeOrders(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
        @RequestParam(required = true) int oid
    ) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            response.put("message", "User doesn't have the permission.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        Map<String, Object> responseData = orderItemService.organizeHostViewOrders(
            oid
        );
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @ExceptionHandler(RequestBodyValidationException.class)
    public ResponseEntity<String> handleValidationException(
        RequestBodyValidationException ex
    ) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handlePermissionException(
        ForbiddenException ex
    ) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(
        EntityNotFoundException ex
    ) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(
        IllegalArgumentException ex
    ) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ex.getMessage());
    }
}
