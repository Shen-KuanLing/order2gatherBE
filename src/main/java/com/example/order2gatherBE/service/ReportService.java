package com.example.order2gatherBE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.order2gatherBE.models.ReportModel;
import com.example.order2gatherBE.repository.ReportRepository;

@Service
public class ReportService {
    
    @Autowired
    private JavaMailSender mailSender;
    public String sentReport(int userID, int hostID, int orderID, String comment) {
        String systemEmail="order2getherofficial@gmail.com";
        
        // retrive host email from database
        String hostEmail="zer05082000@gmail.com";
        
        // check if the email address is valid
        
        try{
            SimpleMailMessage message=new SimpleMailMessage() ;
            message.setFrom(systemEmail);
            message.setTo(hostEmail);
            message.setSubject("[Order2Gether-OID: "+ orderID +"]: Order Report from orderer:"+userID);
            message.setText(comment);
            mailSender.send(message);
            System.out.println(message);
        }catch(Exception e){
            System.out.println("error while sending email!\n"+e);
        }
        
        return "report recieved!";
    }

    @Autowired
    ReportRepository reportRepo;
    public void addReport(ReportModel report){
        reportRepo.addReport(report);
    }

}
