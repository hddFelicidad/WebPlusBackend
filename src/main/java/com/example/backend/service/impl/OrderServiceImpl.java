package com.example.backend.service.impl;

import com.example.backend.data.OrderRepository;
import com.example.backend.po.OrderPo;
import com.example.backend.service.OrderService;
import com.example.backend.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Override
    public List<OrderVo> getAllOrders() {
        List<OrderPo> orderPoList = orderRepository.findAll();
        List<OrderVo> orderVoList = new ArrayList<>();
        for(OrderPo eachOrder: orderPoList){
            OrderVo tempOrder = new OrderVo();
            tempOrder.setId(eachOrder.getOrderId());
            tempOrder.setItemId(eachOrder.getItemId());
            tempOrder.setItemCount(eachOrder.getItemCount());
            tempOrder.setDeadLine(eachOrder.getDeadLine());
            orderVoList.add(tempOrder);
        }
        return orderVoList;
    }

    @Override
    public void insertOrder(OrderVo order) {
        orderRepository.save(new OrderPo(order.getId(), order.getItemId(), order.getItemCount(), order.getDeadLine()));
    }
}
