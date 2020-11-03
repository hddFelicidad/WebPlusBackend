package com.example.backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "订单管理类")
public class OrderController {
    /**
     * 临时插入订单
     */
    @PostMapping()
    @ApiOperation(value = "临时插入订单")
    public void post(@RequestBody OrderVo entity) {
        // TODO: process POST request
    }

    /**
     * 获取所有订单
     */
    @GetMapping()
    @ApiOperation(value = "获取所有订单")
    public List<OrderVo> get() {
        // TODO:
        return null;
    }

    @GetMapping(value = "/plan")
    @ApiOperation(value = "获取订单计划")
    public List<OrderPlanVo> getPlan() {
        // TODO:
        return null;
    }

    /**
     * 获取每个订单在当前时间的完成情况
     */
    @GetMapping(value = "/status")
    @ApiOperation(value = "获取每个订单在当前时间的完成情况")
    public List<OrderStatusVo> getStatus() {
        // TODO:
        return null;
    }

    /**
     * 获取订单对应的生产单
     */
    @GetMapping(value = "/production")
    @ApiOperation(value = "获取订单对应的生产单")
    public List<OrderProductionVo> getProduction() {
        // TODO:
        return null;
    }

}