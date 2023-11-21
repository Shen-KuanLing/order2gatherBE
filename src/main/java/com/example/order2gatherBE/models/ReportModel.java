package com.example.order2gatherBE.models;

import java.security.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class ReportModel {
    private int userID;
    private int hostID;
    private int orderID;
    private Timestamp time;
    private String comment;
    // set
    public void setReport(int uid, int hid, int oid, Timestamp time, String comment) {
        this.userID=uid;
        this.hostID=hid;
        this.orderID=oid;
        this.time=time;
        this.comment=comment;
    }
    
    // get
    public int getUID(){
        return this.userID;
    }
    
    public int getHID(){
        return this.hostID;
    }

    public int getOID(){
        return this.orderID;
    }

    public Timestamp getTimestamp(){
        return this.time;
    }

    public String getComment(){
        return this.comment;
    }

}
