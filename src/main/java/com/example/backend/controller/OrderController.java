package com.example.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.example.backend.vo.OrderStatus;
import com.example.backend.vo.OrderVo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = "/order")
public class OrderController {
    /**
     * 临时插入订单
     */
    @PostMapping()
    public void post(@RequestBody OrderVo entity) {
        // TODO: process POST request
    }

    /**
     * 获取所有订单
     */
    @GetMapping(value = "/all")
    public List<OrderVo> getAll() {
        // TODO:
        return null;
    }

    /**
     * 获取每个订单的完成情况
     */
    @GetMapping(value = "/status/all")
    public List<OrderStatus> getStatus() {
        // TODO:
        return null;
    }

}