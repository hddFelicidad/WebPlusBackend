package com.example.backend.service.impl;

import com.example.backend.vo.OrderVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderServiceImpl orderService;

    @Test
    void getAllOrders(){
        List<OrderVo> orderVoList = orderService.getAllOrders();
        assertEquals(5,orderVoList.size());
    }

    @Test
    void insertOrder(){
        OrderVo order = new OrderVo();
        order.setId("400001");
        order.setItemId("3000001");
        order.setItemCount(10);
        order.setDeadLine(new Date("2020/10/25"));
        orderService.insertOrder(order);
    }
}
