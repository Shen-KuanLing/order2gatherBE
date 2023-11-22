package com.example.order2gatherBE.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.order2gatherBE.models.OrderEventModel;

import javax.sql.DataSource;

@Repository
public class OrderEventRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createOrderEvent(OrderEventModel orderEventModel){
        System.out.println("EXCUTE INSERT OrderEvent");
        jdbcTemplate.update("INSERT INTO orderEvent(rid, hostID, secretcode, stopOrderingTime, estimatedArrivalTime, endEventTime, totalPrice, totalPeople, status) "
                        + "VALUES (?,?,?,?,?,?,?,?,?)", orderEventModel.getRId(), orderEventModel.getHostID(),
                orderEventModel.getSecretCode(), orderEventModel.getStopOrderingTime(), orderEventModel.getEstimatedArrivalTime(), orderEventModel.getEndEventTime(),
                orderEventModel.getTotalPrice(), orderEventModel.getTotalPeople(), orderEventModel.getStatus());
    }
}
