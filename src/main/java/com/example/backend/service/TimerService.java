package com.example.backend.service;

import com.example.backend.vo.ResponseVO;
import com.example.backend.vo.TimerVo;

public interface TimerService {
    ResponseVO updateTimer(TimerVo vo);

    TimerVo getTimer();
}
