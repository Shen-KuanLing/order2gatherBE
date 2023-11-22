package com.example.order2gatherBE.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.example.order2gatherBE.models.OrderEventModel;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class OrderEventRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createOrderEvent(OrderEventModel orderEventModel) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO orderEvent (rid, hostId, secretcode, createTime, stopOrderingTime, estimatedArrivalTime, endEventTime, totalPrice, totalPeople, status) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            // Set parameters
            ps.setInt(1, orderEventModel.getRId());
            ps.setInt(2, orderEventModel.getHostID());
            ps.setString(3, orderEventModel.getSecretCode());
            ps.setTimestamp(4, orderEventModel.getCreateTime());
            ps.setTimestamp(5, orderEventModel.getStopOrderingTime());
            ps.setTimestamp(6, orderEventModel.getEstimatedArrivalTime());
            ps.setTimestamp(7, orderEventModel.getEndEventTime());
            ps.setInt(8, orderEventModel.getTotalPrice());
            ps.setInt(9, orderEventModel.getTotalPeople());
            ps.setInt(10, orderEventModel.getStatus());

            return ps;
        }, keyHolder);

        // Retrieve the DB auto generated order event id
        Integer orderEventId = keyHolder.getKey().intValue();
        orderEventModel.setId(orderEventId);

        // Save member uids in the userOrderFood table
        addMemberList(orderEventModel.getMemberList(), orderEventId);
    }
    private void addMemberList(List<Integer> memberList, Integer oid) {
        String insertQuery = "INSERT INTO userOrderFood (uid, oid, hostViewFoodName) VALUES (?,?,?)";
        for (Integer memberId : memberList) {
            jdbcTemplate.update(insertQuery, memberId, oid, "");
        }
    }
}
