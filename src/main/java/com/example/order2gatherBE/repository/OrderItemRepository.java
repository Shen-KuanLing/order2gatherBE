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

    // get all order items
    public List<OrderItemModel> getAllOrderItem(int uid, int oid){
        System.out.println("EXCUTE getAllItem");
        String sql = "Select * from userOrderFood where  uid = ? and oid =? and isDeleted = false ";
        List<OrderItemModel> orders = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OrderItemModel.class), uid, oid);
        if (orders.size() == 0)
            return null;
        return orders;
    }

    // mark the food item deleted
    public void markDeleted(OrderItemModel item){
        System.out.println("EXCUTE markDeleted");
        jdbcTemplate.update("UPDATE userOrderFood SET isDeleted = true "
                            +"WHERE uid=? and oid=? and fid=?", 
                            item.getUID(), item.getOID(),item.getFID());
    }
  
}
