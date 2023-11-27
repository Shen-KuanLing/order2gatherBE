package com.example.order2gatherBE.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.order2gatherBE.models.OrderItemModel;

import java.util.*;

@Repository
public class OrderItemRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // init a null data in order to track members in an order event ok
    public void initOrderEventMembers(int uid, int oid){
        System.out.println("EXCUTE initOrderEventMembers");
        jdbcTemplate.update("INSERT INTO userOrderFood(uid, oid, foodName, hostViewFoodName, price, hostViewPrice, num, comment, fid) "
                            + "VALUES (?,?,?,?,?,?,?,?,?)",
                            uid, oid, "", "", -1,-1, -1, "", -1);
    }


    // add new item ok
    public void addOrderItem(OrderItemModel item){
        System.out.println("EXCUTE addOrderItem");
        jdbcTemplate.update("INSERT INTO userOrderFood(uid, oid, foodName, hostViewFoodName, price, hostViewPrice, num, comment, fid) "
        + "VALUES (?,?,?,?, ?,?,?,?,?)",
        item.getUID(), item.getOID(),
        item.getFoodName(), item.getHostViewFoodName(),
        item.getPrice(), item.getHostViewPrice(),
        item.getNum(), item.getComment(), item.getFID());
    }

    // update the Database ok
    public void modifyOrderItem(OrderItemModel item){
        if(item.getFID()==-1)
        {
            System.out.println("cannot access member info");
            return;
        }
        System.out.println("EXCUTE modifyOrderItem");
        // update item: search item by uid, oid, and fid
        jdbcTemplate.update("UPDATE userOrderFood SET foodName=?, hostViewFoodName=?, price=?, hostViewPrice=?, num=?, comment=? "
        +"WHERE uid=? and oid=? and fid=?",
        item.getFoodName(), item.getHostViewFoodName(),
        item.getPrice(), item.getHostViewPrice(),
        item.getNum(), item.getComment(),
        item.getUID(), item.getOID(),item.getFID());
    }

    // deleted item ok
    public void deleteOrderItem(OrderItemModel item){
        if(item.getFID()==-1)
        {
            System.out.println("cannot access member info");
            return;
        }
        System.out.println("EXCUTE markDeleted");
        jdbcTemplate.update("DELETE FROM userOrderFood "
        +"WHERE uid=? and oid=? and fid=?",
        item.getUID(), item.getOID(),item.getFID());
    }

    // get existed users(null rows with null string and fid=-1) ok
    public List<Integer> getUsers(int oid){
        System.out.println("EXCUTE getUsers");

        List orders = jdbcTemplate.queryForList("SELECT * FROM userOrderFood WHERE oid=" + oid+ " and fid=-1");
        List<Integer> uids=new ArrayList<Integer>();

        System.out.println(orders.size());
        Iterator it =orders.iterator();
        while(it.hasNext()){
            Map orderMap=(Map)it.next();
            uids.add((Integer)orderMap.get("uid"));
        }
        if(uids.isEmpty())System.out.println("uid not found");
        return uids;
    }

    // get all order items of a user ok
    public List<OrderItemModel> getUserOrderItem(int uid, int oid){
        System.out.println("EXCUTE getAllItem");
        List orders = jdbcTemplate.queryForList("SELECT * FROM userOrderFood WHERE oid=" + oid + " and uid=" + uid);
        List<OrderItemModel> items=new ArrayList<OrderItemModel>();

        System.out.println(orders.size());
        Iterator it =orders.iterator();
        while(it.hasNext()){
            Map itemMap=(Map)it.next();
            if((Integer)itemMap.get("fid")==-1) continue;

            OrderItemModel temp= new OrderItemModel();
            temp.setOrderItem(
                (Integer)itemMap.get("uid"),
                (Integer)itemMap.get("oid"),
                (String)itemMap.get("foodName"),
                (String)itemMap.get("hostViewFoodName"),
                (Integer)itemMap.get("price"),
                (Integer)itemMap.get("hostViewPrice"),
                (Integer)itemMap.get("num"),
                (String)itemMap.get("comment"),
                (Integer)itemMap.get("fid"));

            items.add(temp);
            // System.out.println(temp.getUID()+(String)temp.getFoodName());
        }
        if(items.isEmpty())System.out.println("Order item not found");
        return items;
    }

    // get all order items of an order event ok
    public List<OrderItemModel> getAllOrderItem(int oid){
        System.out.println("EXCUTE getAllItem");
        List orders = jdbcTemplate.queryForList("SELECT * FROM userOrderFood WHERE oid=" + oid);
        List<OrderItemModel> items=new ArrayList<OrderItemModel>();

        System.out.println(orders.size());
        Iterator it =orders.iterator();
        while(it.hasNext()){
            Map itemMap=(Map)it.next();
            if((Integer)itemMap.get("fid")==-1) continue;

            OrderItemModel temp= new OrderItemModel();
            temp.setOrderItem(
                (Integer)itemMap.get("uid"),
                (Integer)itemMap.get("oid"),
                (String)itemMap.get("foodName"),
                (String)itemMap.get("hostViewFoodName"),
                (Integer)itemMap.get("price"),
                (Integer)itemMap.get("hostViewPrice"),
                (Integer)itemMap.get("num"),
                (String)itemMap.get("comment"),
                (Integer)itemMap.get("fid"));

            items.add(temp);
            // System.out.println(temp.getUID()+(String)temp.getFoodName());
        }
        if(items.isEmpty())System.out.println("Order item not found");
        return items;
    }

    public List<OrderItemModel> getHostViewOrderItems(int oid) {
        String sql = "SELECT uof.uid, uof.foodName, uof.hostViewFoodName, uof.hostViewPrice, uof.num, uof.comment " +
                "FROM userOrderFood uof " +
                "WHERE oid = ? AND uof.foodName IS NOT NULL AND uof.hostViewPrice IS NOT NULL";
        return jdbcTemplate.query(sql, new Object[]{oid}, new OrderItemRowMapper());
    }


}
