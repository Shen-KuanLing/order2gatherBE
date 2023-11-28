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

@RestController
public class ReportController {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportModel reportModel;

    @Autowired
    private AuthenticationService authenticationService;

    public String sentReport(int userID, int orderID, String comment) {
        reportService.sentReport(userID, orderID,comment);
        return "report recieved!";
    }

    @PostMapping(path="/report")
    public ResponseEntity<String> receiveReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody ReportModel formData) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            return new ResponseEntity<>("User doesn't have the permission.", HttpStatus.FORBIDDEN);
        }

        // store it into database
        reportModel.setReport(formData.getUID(),  formData.getOID(), formData.getTimestamp(), formData.getComment());
        reportService.addReport(reportModel);

        // activate sentReport() method
        this.sentReport(formData.getUID(), formData.getOID(), formData.getComment());
        String temp= String.format("{\"status\": \"success\", \"comment\": \" %s\"}",formData.getComment());

        // Return a response back to the frontend
        return new ResponseEntity<>(temp,HttpStatus.OK);
    }



}
