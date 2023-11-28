package com.example.order2gatherBE.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.order2gatherBE.models.ReportModel;
import com.example.order2gatherBE.models.UserModel;
import com.example.order2gatherBE.models.OrderEventModel;
import java.util.List;



@Repository // Communicate with DB, use MYSQL
public class ReportRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // insert report to DB
    public void addReport(ReportModel report){
		System.out.println("EXCUTE INSERT ");
        jdbcTemplate.update("INSERT INTO report(uid, oid, time, comment) "+ "VALUES (?,?,?,?)", 
                            report.getUID(), report.getOID(),report.getTimestamp(), report.getComment());
    }
    
    // get gmail from database 
    public String findHostEmail(int oid) {
        // find hostID by oid through OrderEventModel 
        String sql_1 = "Select * from orderEvent where id = ?";
        List<OrderEventModel> temp = jdbcTemplate.query(sql_1 , new BeanPropertyRowMapper<>(OrderEventModel.class), oid);
        int hid=temp.get(0).getHostID();


        // find host's Gmail
        String sql_2 = "Select * from user where id = ?";
        List<UserModel> users = jdbcTemplate.query(sql_2, new BeanPropertyRowMapper<>(UserModel.class), hid);
        if (users.size() == 0)
            return null;
        return users.get(0).getGmail();
    }
}
