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
import com.example.order2gatherBE.service.ReportService;
@RestController
public class ReportController {
    
    @Autowired
    ReportService reportService; 

    @Autowired
    ReportModel reportModel;
    
    int counter=0;
   
    public String sentReport(int userID, int hostID, int orderID, String comment) {
        reportService.sentReport(userID, hostID, orderID,comment);
        return "report recieved!";
    }
    
    @PostMapping(path="/report")
    public String receiveReport(@RequestBody JSONObject formData) {
        
        // recieve data
        System.out.println(formData.get("UID").getClass());
        int uid = Integer.valueOf(formData.get("UID").toString());
        int hid = Integer.valueOf(formData.get("HID").toString());
        int oid = Integer.valueOf(formData.get("OID").toString());
        String content = (String) formData.get("content").toString();

        // store it into database
        // reportModel.setReport(uid, hid, oid, null, content);
        // reportService.addReport(reportModel);
        
        // activate sentReport() method
        this.sentReport(uid, hid, oid, content);
        String temp= String.format("{\"status\": \"success\", \"counter\": \"%d\", \"content\": \" %s\"}",counter++,formData.get("content"));

        // Return a response back to the frontend
        return temp;
        
    }

    
    
}

