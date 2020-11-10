package com.example.backend.controller;

import com.example.backend.service.OrderService;
import com.example.backend.vo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    /**
     * 获取产品甘特图数据（按小时显示）
     */
    @GetMapping(value = "/product/{pid}/{date}")
    public ResponseVO getProductOccupyByHour(@PathVariable("pid") String productId, @PathVariable("date") String date) {
        return orderService.getProductOccupyByHour(productId, date);
    }

    /**
     * 获取产品甘特图数据（按天显示）
     */
    @PostMapping(value = "/product/{pid}")
    public ResponseVO getProductOccupyByDay(@PathVariable("pid") String productId,
            @RequestBody Map<String, String> date) {
        return orderService.getProductOccupyByDay(productId, date);
    }

    @GetMapping(value = "/order/plan")
    public ResponseVO getOrderPlan() {
        return orderService.getOrderPlan();
    }

    @GetMapping(value = "/order/plan/production")
    public ResponseVO getOrderPlanProduction() {
        return orderService.getOrderPlanProduction();
    }

}