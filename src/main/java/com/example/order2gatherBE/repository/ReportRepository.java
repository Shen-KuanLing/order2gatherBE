package com.example.order2gatherBE.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.order2gatherBE.models.ReportModel;
import com.example.order2gatherBE.models.UserModel;
import com.example.order2gatherBE.models.OrderEventModel;
import com.example.order2gatherBE.models.OrderItemModel;
import com.example.order2gatherBE.repository.OrderItemRepository;
import java.sql.Timestamp;

import java.util.*;

@Repository // Communicate with DB, use MYSQL
public class ReportRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    OrderItemRepository orderItemRepository;

    // insert report to DB
    public void addReport(ReportModel report){
		System.out.println("EXCUTE INSERT ");
        jdbcTemplate.update("INSERT INTO report(uid, oid, time, comment) "+ "VALUES (?,?,?,?)",
                            report.getUID(), report.getOID(),report.getTime(), report.getComment());
    }
    public int getHostID(int oid){
        String sql_1 = "Select * from orderEvent where id = ?";
        List<OrderEventModel> temp = jdbcTemplate.query(sql_1 , new BeanPropertyRowMapper<>(OrderEventModel.class), oid);
        return temp.get(0).getHostID();
    }
    // get gmail from database
    public String findHostEmail(int oid) {
        // find hostID by oid through OrderEventModel
        int hid=getHostID(oid);

        // find host's Gmail
        String sql_2 = "Select * from user where id = ?";
        List<UserModel> users = jdbcTemplate.query(sql_2, new BeanPropertyRowMapper<>(UserModel.class), hid);
        if (users.size() == 0)
            return null;
        return users.get(0).getGmail();
    }

    // for host to send notification to orders
    public List<String> findOrdererGmail(int oid) {

        // find userIDs by oid through OrderItemModule
        List<Integer> uid_list= orderItemRepository.getUsers(oid);
        System.out.println(uid_list);
        // find user's Gmail from User Module
        String sql_2 = "Select * from user where id = ?";
        List<String> gmail_list = new ArrayList<String>();
        for(int i =0; i<uid_list.size();i++){
            List<UserModel> users = jdbcTemplate.query(sql_2, new BeanPropertyRowMapper<>(UserModel.class), uid_list.get(i));
            gmail_list.add(users.get(0).getGmail());
        }

        return gmail_list;
    }
    public Dictionary<Integer,String> findOrdererGmail_Dict(int oid) {

        // find userIDs by oid through OrderItemModule
        List<Integer> uid_list= orderItemRepository.getUsers(oid);
        System.out.println(uid_list);
        // find user's Gmail from User Module
        String sql_2 = "Select * from user where id = ?";
        Dictionary<Integer,String> gmail_list = new Hashtable<>();
        for(int i =0; i<uid_list.size();i++){
            List<UserModel> users = jdbcTemplate.query(sql_2, new BeanPropertyRowMapper<>(UserModel.class), uid_list.get(i));
            gmail_list.put(users.get(0).getId(),users.get(0).getGmail());
        }

        return gmail_list;
    }

    // get Report of sent by an order in an order event
    public List<String> getReport(int uid, int oid){
        System.out.println("EXECUTE getReport");
        List reports = jdbcTemplate.queryForList("SELECT * FROM report WHERE uid="+uid+ " and oid=" + oid);
        List<String> comments=new ArrayList<String>();
        Iterator it =reports.iterator();
        while(it.hasNext()){
            Map reportMap=(Map)it.next();
            comments.add((String)reportMap.get("comment"));
        }
        if(comments.isEmpty()) return null;
        return comments;
    }

    // get Report of sent by an order in an order event
    public List<ReportModel> getAllReport(int oid){
        System.out.println("EXECUTE getAllReport");
        List reports = jdbcTemplate.queryForList("SELECT * FROM report WHERE oid=" + oid);
        List<ReportModel> reportList=new ArrayList<ReportModel>();

        Iterator it =reports.iterator();
        while(it.hasNext()){
            Map itemMap=(Map)it.next();

            ReportModel temp= new ReportModel();
            temp.setReport(
                (int)itemMap.get("uid"),
                (int)itemMap.get("oid"),
                (Timestamp)itemMap.get("time"),
                (String)itemMap.get("comment"));
            reportList.add(temp);
            // System.out.println(temp.getUID()+(String)temp.getFoodName());
        }
        return  reportList;

    }

}
