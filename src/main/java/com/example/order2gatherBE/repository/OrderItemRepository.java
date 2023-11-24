package com.example.order2gatherBE.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.order2gatherBE.models.OrderItemModel;

import java.util.List;

@Repository
public class OrderItemRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // init a null data in order to track members in an order event
    public void initOrderEventMembers(int uid, int oid){
        System.out.println("EXCUTE initOrderEventMembers");
        jdbcTemplate.update("INSERT INTO userOrderFood(uid, oid, foodName, hostViewFoodName, price, hostViewPrice, num, comment, fid) "
                            + "VALUES (?,?,?,?,?,?,?,?)", 
                            uid, oid, "", "", -1,-1, -1, "", -1);
    }

    // add new item 
    public void addOrderItem(OrderItemModel item){
        System.out.println("EXCUTE addOrderItem");
        jdbcTemplate.update("INSERT INTO userOrderFood(uid, oid, foodName, hostViewFoodName, price, hostViewPrice, num, comment, fid) "
        + "VALUES (?,?,?,?,?,?,?,?)", 
        item.getUID(), item.getOID(),
        item.getFoodName(), item.getHostViewFoodName(),
        item.getPrice(), item.getHostViewPrice(),
        item.getNum(), item.getComment(), item.getFID());
    }
    
    // update the Database
    public void modifyOrderItem(OrderItemModel item){
        System.out.println("EXCUTE modifyOrderItem");
        // update item: search item by uid, oid, and fid
        jdbcTemplate.update("UPDATE userOrderFood SET foodName=?, hostViewFoodName=?, price=?, hostViewPrice=?, num=?, comment=? "
        +"WHERE uid=? and oid=? and fid=?", 
        item.getFoodName(), item.getHostViewFoodName(),
        item.getPrice(), item.getHostViewPrice(),
        item.getNum(), item.getComment(),
        item.getUID(), item.getOID(),item.getFID());
    }
    
    // deleted item
    public void deleteOrderItem(OrderItemModel item){
        System.out.println("EXCUTE markDeleted");
        jdbcTemplate.update("DELETE FROM userOrderFood "
                            +"WHERE uid=? and oid=? and fid=?", 
                            item.getUID(), item.getOID(),item.getFID());
    }

    // get all order items of a user
    public List<OrderItemModel> getUserOrderItem(int uid, int oid){
        System.out.println("EXCUTE getAllItem");
        String sql = "Select * from userOrderFood where  uid = ? and oid =?";
        List<OrderItemModel> orders = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OrderItemModel.class), uid, oid);
        if (orders.size() == 0)
            return null;
        return orders;
    }
    
    // get all order items of a order event
    public List<OrderItemModel> getAllOrderItem(int oid){
        System.out.println("EXCUTE getAllItem");
        String sql = "Select * from userOrderFood where oid =?";
        List<OrderItemModel> orders = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OrderItemModel.class),oid);
        if (orders.size() == 0)
            return null;
        return orders;
    }

  
}
