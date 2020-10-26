package com.example.backend.service;

import com.example.backend.vo.OrderVo;

import java.util.List;

public interface OrderService {

    List<OrderVo> getAllOrders();

    int insertOrder(OrderVo order);
}
