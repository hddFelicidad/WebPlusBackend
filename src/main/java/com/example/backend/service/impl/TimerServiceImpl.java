package com.example.backend.service.impl;

import java.sql.Timestamp;

import com.example.backend.data.TimerRepositroy;
import com.example.backend.po.TimerPo;
import com.example.backend.service.TimerService;
import com.example.backend.vo.TimerVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.var;

@Service
public class TimerServiceImpl implements TimerService {
    @Autowired
    TimerRepositroy repository;

    @Override
    public void updateTimer(TimerVo vo) {
        repository.deleteAll();
        repository.save(new TimerPo(null, new Timestamp(vo.getInitTime().getTime()), vo.getRate()));
    }

    @Override
    public TimerVo getTimer() {
        var timers = repository.findAll();
        if (timers.isEmpty())
            throw new RuntimeException("计时器尚未初始化");
        var po = timers.get(0);
        return new TimerVo(po.getInitTime(), po.getRate());
    }

}