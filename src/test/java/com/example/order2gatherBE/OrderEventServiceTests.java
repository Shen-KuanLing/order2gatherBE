package com.example.order2gatherBE;

import com.example.order2gatherBE.models.OrderEventModel;
import com.example.order2gatherBE.models.UserModel;
import com.example.order2gatherBE.repository.OrderEventRepository;
import com.example.order2gatherBE.services.OrderEventService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@DisplayName("Order event test")
public class OrderEventServiceTests {
    @Autowired
    private OrderEventService orderEventService;
    @MockBean
    private OrderEventRepository orderEventRepository;
    private int oid;
    private String secretCode;
    private String rName;
    private Timestamp mockCreateTime;
    private Timestamp mockEstimatedArrivalTime;
    private Timestamp mockStopOrderingTime;
    private Timestamp mockEndEventTime;
    @BeforeEach
    void init() {
        oid = 2;
        secretCode ="123456";
        rName = "測試餐廳";
        mockCreateTime = Timestamp.valueOf(LocalDateTime.of(2023, 11, 24, 12, 30, 0));
        mockStopOrderingTime = Timestamp.valueOf(LocalDateTime.of(2023, 11, 24, 14, 30, 0));
        mockEndEventTime = Timestamp.valueOf(LocalDateTime.of(2023, 11, 25, 12, 30, 0));
        mockEstimatedArrivalTime = Timestamp.valueOf(LocalDateTime.of(2023, 11, 24, 15, 00, 0));
    }

    OrderEventModel constructOrderEvent(int rid, String rName, int hostId, Timestamp createTime, Timestamp stopOrderingTime, Timestamp endEventTime,
                                     Timestamp estimatedArrivalTime, int totalPrice, int totalPeople, int status, String secretCode) {
        OrderEventModel orderEvent = new OrderEventModel();
        orderEvent.setRid(rid);
        orderEvent.setRName(rName);
        orderEvent.setHostID(hostId);
        orderEvent.setCreateTime(createTime);
        orderEvent.setStopOrderingTime(stopOrderingTime);
        orderEvent.setEndEventTime(endEventTime);
        orderEvent.setEstimatedArrivalTime(estimatedArrivalTime);
        orderEvent.setTotalPrice(totalPrice);
        orderEvent.setTotalPeople(totalPeople);
        orderEvent.setStatus(status);
        orderEvent.setSecretCode(secretCode);
        return orderEvent;
    }

    @Test
    @DisplayName(value="[UNIT TEST]: Create order event")
    void createOrderEvent() {
        OrderEventModel orderEvent = constructOrderEvent(1, null, 2, mockCreateTime, mockStopOrderingTime,
                mockEndEventTime, mockEstimatedArrivalTime, 100, 2, 1, null);
        Mockito.when(orderEventRepository.createOrderEvent(orderEvent)).thenReturn(1);

        Assertions.assertEquals(orderEventService.createOrderEvent(orderEvent), 1);
    }
    @Test
    @DisplayName(value="[UNIT TEST]: Get order event by oid")
    void getOrderEventByOid() {
        OrderEventModel orderEvent = constructOrderEvent(1, rName, 2, mockCreateTime, mockStopOrderingTime,
                mockEndEventTime, mockEstimatedArrivalTime, 100, 2, 1, secretCode);
        Mockito.when(orderEventRepository.getOrderEventByOid(oid)).thenReturn(orderEvent);

        Assertions.assertEquals(orderEventService.getOrderEventByOid(oid), orderEvent);
    }
}
