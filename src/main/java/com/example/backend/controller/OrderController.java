package com.example.backend.controller;

import com.example.backend.service.OrderService;
import com.example.backend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * 获取订单甘特图数据
     */
    @GetMapping(value = "/order/{date}")
    public ResponseVO getOrderOccupy(@PathVariable("date") String date) {
        return orderService.getOrderOccupy(date);
    }

}