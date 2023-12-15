package com.example.order2gatherBE.controllers;

import java.sql.Timestamp;

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

import com.example.order2gatherBE.models.OrderEventModel;
import com.example.order2gatherBE.models.ReportModel;
import com.example.order2gatherBE.repository.ReportRepository;
import com.example.order2gatherBE.services.AuthenticationService;
import com.example.order2gatherBE.services.OrderEventService;
import com.example.order2gatherBE.services.ReportService;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.*;

@RestController
public class ReportController {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    OrderEventService orderEventService;


    @Autowired
    private AuthenticationService authenticationService;

    // send report to the host
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

    // send notification to orderers in an order event
    @PostMapping(path="/notify")
    public ResponseEntity<String> notifyOrderer(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody ReportModel formData) {
        token = token.replace("Bearer ", "");
        int uid = authenticationService.verify(token);
        if (uid == -1) {
            return new ResponseEntity<>("User doesn't have the permission.", HttpStatus.FORBIDDEN);
        }

        // store notification into database
        reportService.addReport(formData);

        // activate sentNotification() method
        reportService.sentNotification(formData.getUID(), formData.getOID(), formData.getComment());
        String temp= String.format("{\"status\": \"success\", \"comment\": \" %s\"}",formData.getComment());

        // Return a response back to the frontend
        return new ResponseEntity<>(temp,HttpStatus.OK);
    }

    @GetMapping(path="/getUserReport")
    public ResponseEntity<List<String>> getReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam int uid, @RequestParam int oid ) {
        token = token.replace("Bearer ", "");
        int auth_uid = authenticationService.verify(token);
        if (auth_uid == -1) {
            return null;
        }
        List<String> comments=reportService.getReport(uid,oid);
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }
    @GetMapping(path="/getGmail")
    public ResponseEntity<Dictionary<Integer,String>> getGmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam int oid ) {
        token = token.replace("Bearer ", "");
        int auth_uid = authenticationService.verify(token);
        if (auth_uid == -1) {
            return null;
        }
        return new ResponseEntity<>(reportService.findGmail(oid),HttpStatus.OK);

    }
    // get all report in an order event
    @GetMapping(path="/getAllReport")
    public ResponseEntity<List<ReportModel>> getAllReport(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam int hid ) {
        token = token.replace("Bearer ", "");
        int auth_uid = authenticationService.verify(token);
        if (auth_uid == -1) {
            return null;
        }
        // get oidList of hid
        List<OrderEventModel> orderEventList= orderEventService.getOrderEventByUid(hid);
        List<Integer> oidList=new ArrayList<Integer>();
        System.out.println(orderEventList.size());

        for(int i =0;i<orderEventList.size();i++){
            System.out.println(orderEventList.get(i).getId());
            if(hid==orderEventList.get(i).getHostID())
                oidList.add(orderEventList.get(i).getId());
        }

        // get all report
        List<ReportModel> reportList=new ArrayList<ReportModel>();
        for(int i =0;i<oidList.size();i++){
            reportList.addAll(reportService.getAllReport(oidList.get(i)));
        }

        return new ResponseEntity<>(reportList,HttpStatus.OK);
    }


}
