package com.example.order2gatherBE.models;

import java.sql.Timestamp;

// import java.security.Timestamp;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
public class ReportModel {
    private int uid;
    private int oid;
    private Timestamp time;
    private String comment;
    // set
    public void setReport(int uid, int oid, Timestamp time, String comment) {
        this.uid=uid;
        this.oid=oid;
        this.time=time;
        this.comment=comment;
    }

    // get
    public int getUID(){
        return this.uid;
    }
    public int getOID(){
        return this.oid;
    }

    public Timestamp getTime(){
        return this.time;
    }

    public String getComment(){
        return this.comment;
    }

}
