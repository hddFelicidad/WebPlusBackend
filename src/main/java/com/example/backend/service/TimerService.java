package com.example.backend.service;

import com.example.backend.vo.TimerVo;

public interface TimerService {
    void updateTimer(TimerVo vo);

    TimerVo getTimer();
}