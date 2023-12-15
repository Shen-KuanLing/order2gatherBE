package com.example.order2gatherBE.models;

import java.sql.Timestamp;

public class HistoryModel {
    private Integer oid;
    private String rName;
    private Integer hostID;
    private String hostName;
    private String hostGmail;
    private Timestamp createTime;
    private Timestamp stopOrderingTime;
    private Timestamp endEventTime;
    private Timestamp estimatedArrivalTime;

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getRName() {
        return rName;
    }

    public void setRName(String rName) {
        this.rName = rName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostGmail() {
        return hostGmail;
    }

    public void setHostGmail(String hostGmail) {
        this.hostGmail = hostGmail;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getStopOrderingTime() {
        return stopOrderingTime;
    }

    public void setStopOrderingTime(Timestamp stopOrderingTime) {
        this.stopOrderingTime = stopOrderingTime;
    }

    public Timestamp getEndEventTime() {
        return endEventTime;
    }

    public void setEndEventTime(Timestamp endEventTime) {
        this.endEventTime = endEventTime;
    }

    public Timestamp getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(Timestamp estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }
}
