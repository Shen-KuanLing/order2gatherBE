package com.example.order2gatherBE.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public int createOrderEvent(OrderEventModel orderEventModel) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int createStatus = jdbcTemplate.update(connection -> {
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
        }, keyHolder) > 0 ? 1 : 0;

        // Retrieve the DB auto generated order event id
        Integer orderEventId = keyHolder.getKey().intValue();
        orderEventModel.setId(orderEventId);

        // Save member uids in the userOrderFood table
        int addMemberStatus = addMemberList(orderEventModel.getMemberList(), orderEventId);
        return createStatus*addMemberStatus;
    }
    public int addMemberList(List<Integer> memberList, Integer oid) {
        try {
            String insertQuery = "INSERT INTO userOrderFood (uid, oid, hostViewFoodName) VALUES (?,?,?)";
            for (Integer memberId : memberList) {
                jdbcTemplate.update(insertQuery, memberId, oid, "");
            }
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
    public OrderEventModel getOrderEventByOid(Integer oid) {
        String sql = "SELECT oe.id, oe.rid, oe.hostId, oe.secretcode, oe.createTime, oe.stopOrderingTime, "
                + "oe.endEventTime, oe.estimatedArrivalTime, oe.totalPrice, oe.totalPeople, oe.status, r.name AS restaurantName, u.gmail AS hostGmail "
                + "FROM orderEvent oe JOIN restaurant r ON oe.rid = r.id JOIN user u ON oe.hostId = u.id WHERE oe.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{oid}, new OrderEventRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public List<OrderEventModel> getOrderEventByUid(Integer uid) {
        String sql = "SELECT oe.*, (SELECT r.name FROM restaurant r WHERE r.id = oe.rid) AS restaurantName, u.gmail AS hostGmail "
                + "FROM orderEvent oe "
                + "JOIN userOrderFood uof ON oe.id = uof.oid "
                + "JOIN user u ON oe.hostId = u.id "
                + "WHERE uof.uid = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{uid}, new OrderEventRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    public void updateOrderEvent(OrderEventModel updatedOrderEvent) {
        String sql = "UPDATE orderEvent SET stopOrderingTime = ?, estimatedArrivalTime = ?, endEventTime = ?, " +
                "status = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                updatedOrderEvent.getStopOrderingTime(),
                updatedOrderEvent.getEstimatedArrivalTime(),
                updatedOrderEvent.getEndEventTime(),
                updatedOrderEvent.getStatus(),
                updatedOrderEvent.getId());
    }
    public Integer getOidBySecretCode(String secretCode) {
        String sql = "SELECT id FROM orderEvent WHERE secretcode = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{secretCode}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }
    public boolean isUserInOrderEvent(Integer uid, Integer oid) {
        String sql = "SELECT COUNT(*) FROM userOrderFood WHERE uid = ? AND oid = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{uid, oid}, Integer.class) > 0;
    }
    public void updateOrderEventTotalPeople(Integer oid) {
        String sql = "UPDATE orderEvent SET totalPeople = totalPeople + 1 WHERE id = ?";
        jdbcTemplate.update(sql, oid);
    }
}
