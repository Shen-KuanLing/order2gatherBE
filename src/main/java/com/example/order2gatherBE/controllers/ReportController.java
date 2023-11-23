package com.example.order2gatherBE.controllers;

import java.security.Timestamp;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import com.example.order2gatherBE.models.ReportModel;
import com.example.order2gatherBE.services.ReportService;

@RestController
public class ReportController {
    
    @Autowired
    ReportService reportService; 

    @Autowired
    ReportModel reportModel;
    
    int counter=0;
   
    public String sentReport(int userID, int orderID, String comment) {
        reportService.sentReport(userID, orderID,comment);
        return "report recieved!";
    }
    
    @PostMapping(path="/report")
    public String receiveReport(@RequestBody ReportModel formData) {
        
        // store it into database
        reportModel.setReport(formData.getUID(),  formData.getOID(), formData.getTimestamp(), formData.getComment());
        reportService.addReport(reportModel);
        
        // activate sentReport() method
        this.sentReport(formData.getUID(), formData.getOID(), formData.getComment());
        String temp= String.format("{\"status\": \"success\", \"counter\": \"%d\", \"comment\": \" %s\"}",counter++,formData.getComment());

        // Return a response back to the frontend
        return temp;
        
    }

    
    
}

