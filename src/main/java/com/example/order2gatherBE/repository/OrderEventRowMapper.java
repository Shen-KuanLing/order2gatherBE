package com.example.order2gatherBE.repository;

import com.example.order2gatherBE.models.OrderEventModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class OrderEventRowMapper implements RowMapper<OrderEventModel> {

    @Override
    public OrderEventModel mapRow(ResultSet rs, int rowNum)
        throws SQLException {
        OrderEventModel orderEventModel = new OrderEventModel();
        orderEventModel.setId(rs.getInt("id"));
        orderEventModel.setRid(rs.getInt("rid"));
        orderEventModel.setRName(rs.getString("restaurantName"));
        orderEventModel.setHostID(rs.getInt("hostId"));
        orderEventModel.setHostGmail(rs.getString("hostGmail"));
        orderEventModel.setSecretCode(rs.getString("secretcode"));
        orderEventModel.setCreateTime(rs.getTimestamp("createTime"));
        orderEventModel.setStopOrderingTime(
            rs.getTimestamp("stopOrderingTime")
        );
        orderEventModel.setEndEventTime(rs.getTimestamp("endEventTime"));
        orderEventModel.setEstimatedArrivalTime(
            rs.getTimestamp("estimatedArrivalTime")
        );
        orderEventModel.setTotalPrice(rs.getInt("totalPrice"));
        orderEventModel.setTotalPeople(rs.getInt("totalPeople"));
        orderEventModel.setStatus(rs.getInt("status"));
        return orderEventModel;
    }
}
