package com.example.backend.service;

import com.example.backend.vo.ResponseVO;

public interface ResourceService {

    ResponseVO getOccupy(String date);
}