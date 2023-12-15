package com.example.order2gatherBE.controllers;

import com.example.order2gatherBE.models.HistoryModel;
import com.example.order2gatherBE.services.AuthenticationService;
import com.example.order2gatherBE.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/history", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class HistoryController {
    @Autowired
    HistoryService historyService;
    @Autowired
    private AuthenticationService authenticationService;
    Map<String, String> response = new HashMap<>();

    @GetMapping("/view")
    public ResponseEntity<?> getOrderHistory(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            response.put("message", "User doesn't have the permission.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        List<HistoryModel> historyModels = historyService.getUserOrderEventHistory(uid);
        if (!historyModels.isEmpty()) {
            return new ResponseEntity<>(historyModels, HttpStatus.OK);
        } else {
            String message = String.format("User %d doesn't join any order event so far.", uid);
            response.put("message", message);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
