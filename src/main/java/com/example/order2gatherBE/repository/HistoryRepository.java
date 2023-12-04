package com.example.order2gatherBE.repository;

import com.example.order2gatherBE.models.HistoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HistoryRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<HistoryModel> getUserOrderEventHistory(Integer uid) {
        String sql = "SELECT DISTINCT uof.oid, r.name AS restaurantName, u.username AS hostName, u.gmail AS hostGmail, "
                + "oe.createTime, oe.stopOrderingTime, oe.endEventTime, oe.estimatedArrivalTime FROM userOrderFood uof "
                + "JOIN orderEvent oe ON uof.oid = oe.id "
                + "JOIN restaurant r ON oe.rid = r.id "
                + "JOIN user u ON oe.hostId = u.id "
                + "WHERE uof.uid = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{uid}, new HistoryRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
