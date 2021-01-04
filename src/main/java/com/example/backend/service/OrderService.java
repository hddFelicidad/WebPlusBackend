package com.example.backend.service;

import com.example.backend.vo.ResponseVO;

import java.util.Map;

public interface OrderService {

    ResponseVO getOrderOccupy(String date);

    ResponseVO getProductOccupyByHour(String productId, String date);

    ResponseVO getProductOccupyByDay(String productId, Map<String, String> date);

    ResponseVO getOrderPlan();

    ResponseVO getOrderPlanProduction();

    ResponseVO getAllProduct();
}
