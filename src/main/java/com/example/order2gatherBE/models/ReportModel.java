package com.example.order2gatherBE.models;

import java.security.Timestamp;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

@Component
public class ReportModel {
    @NotNull
    private int userID;
    @NotNull
    private int hostID;
    @NotNull
    private int orderID;
    @NotNull
    private Timestamp time;
    @NotNull    
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
