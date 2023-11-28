package com.example.order2gatherBE.models;

import java.security.Timestamp;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

@Component
public class ReportModel {
    @NotNull
    private int uid;
   @NotNull
    private int oid;
    @NotNull
    private Timestamp time;
    @NotNull
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

    public Timestamp getTimestamp(){
        return this.time;
    }

    public String getComment(){
        return this.comment;
    }

}
