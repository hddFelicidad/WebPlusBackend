package com.example.backend.service;

import com.example.backend.vo.ResponseVO;

public interface OrderService {

    ResponseVO getOrderOccupy(String date);
}
