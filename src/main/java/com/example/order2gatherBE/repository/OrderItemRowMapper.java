package com.example.order2gatherBE.repository;
import com.example.order2gatherBE.models.OrderItemModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper<OrderItemModel> {

    @Override
    public OrderItemModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItemModel orderItemModel = new OrderItemModel();
        orderItemModel.setUid(rs.getInt("uid"));
        orderItemModel.setUserName(rs.getString("username"));
        orderItemModel.setFID(rs.getInt("fid"));
        orderItemModel.setFoodName(rs.getString("foodName"));
        orderItemModel.setHostViewFoodName(rs.getString("hostViewFoodName"));
        orderItemModel.setPrice(rs.getInt("price"));
        orderItemModel.setHostViewPrice(rs.getInt("hostViewPrice"));
        orderItemModel.setNum(rs.getInt("num"));
        orderItemModel.setComment(rs.getString("comment"));
        return orderItemModel;
    }
}
