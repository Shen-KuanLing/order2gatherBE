package com.example.order2gatherBE.models;

import java.sql.Timestamp;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderEventModel {
    private Integer id;

    @NotNull
    private Integer rid;

    private String rName;
    private Integer hostID;
    private List<Integer> memberList;

    @NotEmpty
    private String secretCode;

    private Timestamp createTime;
    private Timestamp stopOrderingTime;
    private Timestamp endEventTime;
    private Timestamp estimatedArrivalTime;
    private Integer totalPrice;
    private Integer totalPeople;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRId() {
        return rid;
    }

    public String getRName() {
        return rName;
    }

    public void setRName(String rName) {
        this.rName = rName;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getHostID() {
        return hostID;
    }

    public void setHostID(Integer hostID) {
        this.hostID = hostID;
    }

    public List<Integer> getMemberList() {
        return memberList;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
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

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(Integer totalPeople) {
        this.totalPeople = totalPeople;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
