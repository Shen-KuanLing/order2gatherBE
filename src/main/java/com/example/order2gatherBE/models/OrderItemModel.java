package com.example.order2gatherBE.models;

import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderItemModel {
    // `uid` integer NOT NULL,
    // `oid` integer NOT NULL,
    // `foodName` varchar(255) DEFAULT "",
    // `hostViewFoodName` varchar(255) NOT NULL,
    // `price` integer DEFAULT 0,
    // `hostViewPrice` integer,
    // `num` integer DEFAULT 0,
    // `comment` varchar(255)
    @NotNull
    private int uid;
    @NotNull
    private int oid;
    private String foodName="";
    @NotNull
    private String hostViewFoodName;
    private int price=0;
    private int hostViewPrice;
    private int num=0;
    private String comment="";

    // ##### do we need this cols? #####
    @NotNull
    private int fid;//food ID
    // private boolean isDeleted=false;

    // set
    public void setOrderItem(   int uid, int oid, String foodName, String hostViewFoodName,
                                int price, int hostViewPrice, int num, String comment,int fid){
        this.uid = uid;
        this.oid = oid;
        this.foodName = foodName;
        this.hostViewFoodName = hostViewFoodName;
        this.price = price;
        this.hostViewPrice = hostViewPrice;
        this.num = num;
        this.comment = comment;

        // new key
        this.fid=fid;

    }

    // get
    public int getUID(){
        return this.uid;
    }
    public void setUid(int uid){
        this.uid = uid;
    }
    public int getOID(){
        return this.oid;
    }

    public String getFoodName(){
        return this.foodName;
    }
    public void setFoodName(String foodName){
        this.foodName = foodName;
    }
    public String getHostViewFoodName(){
        return this.hostViewFoodName;
    }
    public void setHostViewFoodName(String hostViewFoodName){
        this.hostViewFoodName = hostViewFoodName;
    }
    public int getPrice(){
        return this.price;
    }
    public int getHostViewPrice(){
        return this.hostViewPrice;
    }
    public void setHostViewPrice(int hostViewPrice){
        this.hostViewPrice = hostViewPrice;
    }
    public int getNum(){
        return this.num;
    }
    public void setNum(int num){
        this.num = num;
    }
    public String getComment(){
        return this.comment;
    }

    // new key
    public int getFID(){
        return this.fid;
    }

    public Map<String, Object> mapHostViewOrderItem() {
        Map<String, Object> orderItemMap = new HashMap<>();
        orderItemMap.put("foodName", this.foodName);
        orderItemMap.put("hostViewFoodName", this.hostViewFoodName);
        orderItemMap.put("hostViewPrice", this.hostViewPrice);
        orderItemMap.put("num", this.num);
        orderItemMap.put("comment", this.comment);
        return orderItemMap;
    }

    //need this?
    // public boolean isDeleted(){
    //     return this.isDeleted;
    // }
}
