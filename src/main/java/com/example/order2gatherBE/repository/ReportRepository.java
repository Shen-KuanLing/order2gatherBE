package com.example.order2gatherBE.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.order2gatherBE.models.ReportModel;
// import javax.sql.DataSource;


@Repository // Communicate with DB, use MYSQL
public class ReportRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void addReport(ReportModel report){
	
		System.out.println("EXCUTE INSERT ");
        jdbcTemplate.update("INSERT INTO report(uid, hid, oid, timestamp, comment) "+ "VALUES (?,?,?,?,?)", 
                            report.getUID(), report.getHID(), report.getOID(),report.getTimestamp(), report.getComment());
  
    }

}
