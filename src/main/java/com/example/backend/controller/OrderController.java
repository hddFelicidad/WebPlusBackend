package com.example.backend.controller;

import com.example.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.example.backend.vo.OrderPlanVo;
import com.example.backend.vo.OrderProductionVo;
import com.example.backend.vo.OrderStatusVo;
import com.example.backend.vo.OrderVo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * 临时插入订单
     */
    @PostMapping()
    public void post(@RequestBody OrderVo entity) {
        // TODO: process POST request
        orderService.insertOrder(entity);
    }

    /**
     * 获取所有订单
     */
    @GetMapping()
    public List<OrderVo> getAllOrders() {
        // TODO:
        return orderService.getAllOrders();
    }

    /**
     * 获取排程后的订单计划信息：拆分情况以及子订单完成时间
     */
    @GetMapping(value = "/plan")
    public List<OrderPlanVo> getPlan() {
        // TODO:
        return null;
    }

    /**
     * 获取每个订单在当前时间的完成情况
     */
    @GetMapping(value = "/status")
    public List<OrderStatusVo> getStatus() {
        // TODO:
        return null;
    }

    /**
     * 获取订单对应的生产单
     */
    @GetMapping(value = "/production")
    public List<OrderProductionVo> getProduction() {
        // TODO:
        return null;
    }

}