package com.example.order2gatherBE.repository;

import com.example.order2gatherBE.models.HistoryModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryRowMapper implements RowMapper<HistoryModel> {
    @Override
    public HistoryModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        HistoryModel historyModel = new HistoryModel();
        historyModel.setOid(rs.getInt("oid"));
        historyModel.setRName(rs.getString("restaurantName"));
        historyModel.setHostName(rs.getString("hostName"));
        historyModel.setHostGmail(rs.getString("hostGmail"));
        historyModel.setCreateTime(rs.getTimestamp("createTime"));
        historyModel.setStopOrderingTime(rs.getTimestamp("stopOrderingTime"));
        historyModel.setEndEventTime(rs.getTimestamp("endEventTime"));
        historyModel.setEstimatedArrivalTime(rs.getTimestamp("estimatedArrivalTime"));
        return historyModel;
    }
}
