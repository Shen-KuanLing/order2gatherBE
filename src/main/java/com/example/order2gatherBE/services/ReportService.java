package com.example.order2gatherBE.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.order2gatherBE.models.ReportModel;
import com.example.order2gatherBE.repository.AuthenticationRepository;
import com.example.order2gatherBE.repository.ReportRepository;

import java.util.*;
@Service
public class ReportService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    ReportRepository reportRepo;

    private String systemEmail="order2getherofficial@gmail.com";

    // send report through gmail
    public String sentReport(int userID, int orderID, String comment) {

        // retrive host email from database
        String hostEmail=reportRepo.findHostEmail(orderID);

        try{
            // sending email
            SimpleMailMessage message=new SimpleMailMessage() ;
            message.setFrom(systemEmail);
            message.setTo(hostEmail);
            message.setSubject("[Order2Gether-OID: "+ orderID +"]: Order Report from orderer:"+userID);
            message.setText(comment);
            mailSender.send(message);
            System.out.println(message);
        }catch(Exception e){
            System.out.println("error while sending email!\n"+e);
            return "something wrong";
        }

        return "report recieved!";
    }
    public List<String> findGmail(int oid) {
        return reportRepo.findOrdererGmail(oid);
    }
    // send notification through gmail
    public String sentNotification(int userID, int orderID, String comment) {

        // retrieve orderer's emails from database through
        List<String> ordererEmail_list=reportRepo.findOrdererGmail(orderID);
        System.out.println(ordererEmail_list);
        // sending email
        try{
            for(int i =0;i <ordererEmail_list.size();i++){
                SimpleMailMessage message=new SimpleMailMessage() ;
                message.setFrom(systemEmail);
                message.setTo(ordererEmail_list.get(i));
                message.setSubject("[ Order2Gether-OID-"+ orderID +" ]: Order Notification from host:"+userID);
                message.setText(comment);
                mailSender.send(message);
                System.out.println(message);
            }
        }catch(Exception e){
            System.out.println("error while sending email!\n"+e);
            return "something wrong";
        }

        return "report recieved!";
    }

    // add report to the DB
    public void addReport(ReportModel report){
        reportRepo.addReport(report);
    }

    public List<String> getReport(int uid, int oid){
        return reportRepo.getReport(uid, oid);
    }

    // get all report
    public List<ReportModel> getAllReport(int oid){
        return reportRepo.getAllReport(oid);
    }

}
