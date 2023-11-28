package com.example.order2gatherBE.controllers;

import java.security.Timestamp;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.order2gatherBE.models.ReportModel;
import com.example.order2gatherBE.services.AuthenticationService;
import com.example.order2gatherBE.services.ReportService;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.*;

@RestController
public class ReportController {

    @Autowired
    ReportService reportService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(path="/report")
    public ResponseEntity<String> receiveReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody ReportModel formData) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            return new ResponseEntity<>("User doesn't have the permission.", HttpStatus.FORBIDDEN);
        }

        // store it into database
        reportService.addReport(formData);

        // activate sentReport() method
        reportService.sentReport(formData.getUID(), formData.getOID(), formData.getComment());
        String temp= String.format("{\"status\": \"success\", \"comment\": \" %s\"}",formData.getComment());

        // Return a response back to the frontend
        return new ResponseEntity<>(temp,HttpStatus.OK);
    }

    @GetMapping(path="/getReport")
    public ResponseEntity<List<String>> getReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam int uid, @RequestParam int oid ) {
        token = token.replace("Bearer ", "");
        int auth_uid = authenticationService.verify(token);
        if (auth_uid == -1) {
            return null;
        }
        List<String> comments=reportService.getReport(uid,oid);
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }



}
