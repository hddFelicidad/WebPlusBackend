package com.example.backend.service.impl;

import com.example.backend.vo.OrderVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderServiceImpl orderService;

    @Test
    void getAllOrders(){
        List<OrderVo> orderVoList = orderService.getAllOrders();
        assertEquals(78,orderVoList.size());
    }
}
