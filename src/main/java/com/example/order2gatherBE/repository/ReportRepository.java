package com.example.order2gatherBE.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.order2gatherBE.models.ReportModel;
import com.example.order2gatherBE.models.UserModel;

import javax.sql.DataSource;
import java.util.List;



@Repository // Communicate with DB, use MYSQL
public class ReportRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // insert report to DB
    public void addReport(ReportModel report){
		System.out.println("EXCUTE INSERT ");
        jdbcTemplate.update("INSERT INTO report(uid, hid, oid, timestamp, comment) "+ "VALUES (?,?,?,?,?)", 
                            report.getUID(), report.getHID(), report.getOID(),report.getTimestamp(), report.getComment());
  
    }
    
    // get gmail from database 
    @Autowired
    public String findHostEmail(int hid) {
        String sql = "Select * from user where id = ?";
        List<UserModel> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserModel.class), hid);
        if (users.size() == 0)
            return null;
        return users.get(0).getGmail();
    }
}
