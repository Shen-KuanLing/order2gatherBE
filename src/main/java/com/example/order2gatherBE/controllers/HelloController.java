package com.example.order2gatherBE.controllers;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;

// import org.json.simple.*;
import org.json.simple.JSONObject;
// import org.json.*;

import com.example.order2gatherBE.models.ReportModel;
import com.example.order2gatherBE.services.ReportService;

@RestController
public class HelloController {
    int counter=0;
    @Autowired
    ReportModel reportModel;
    
    @Autowired
    ReportService reportService;
    
    public String sentReport(int userID, int hostID, int orderID, String comment) {
        reportService.sentReport(userID, hostID, orderID,comment);
        return "report recieved!";
    }
    
    @PostMapping("/hello")
    public String processFormData( @RequestBody JSONObject formData) {
        // Process the data or perform any backend tasks
        // ...        
        
        counter++;
        System.out.println(formData.get("UID").getClass());
        int uid=Integer.valueOf(formData.get("UID").toString());
        int hid=Integer.valueOf(formData.get("HID").toString());
        int oid=Integer.valueOf(formData.get("OID").toString());
        String content=(String)formData.get("content").toString();
        sentReport(uid, hid, oid, content);
        // reportModel.setReport(uid, hid, oid, null, content);
        // reportService.addReport(reportModel);
        String temp= String.format("{\"status\": \"success\", \"counter\": \"%d\", \"content\": \" %s\"}",counter,formData.get("content"));
        // Return a response back to the frontend
        return temp;
    }
}

