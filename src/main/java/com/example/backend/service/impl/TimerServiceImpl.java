package com.example.backend.service.impl;

import java.sql.Timestamp;

import com.example.backend.data.TimerRepository;
import com.example.backend.po.TimerPo;
import com.example.backend.service.ScheduleInitService;
import com.example.backend.service.TimerService;
import com.example.backend.vo.ResponseVO;
import com.example.backend.vo.TimerVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.var;

@Service
public class TimerServiceImpl implements TimerService {
    @Autowired
    TimerRepository repository;
    @Autowired
    ScheduleInitService scheduleInitService;

    @Override
    public ResponseVO updateTimer(TimerVo vo) {
        repository.deleteAll();
        repository.save(new TimerPo(null, new Timestamp(vo.getInitTime().getTime())));
        return scheduleInitService.scheduleInit(vo.getInitTime());
    }

    @Override
    public TimerVo getTimer() {
        var timers = repository.findAll();
        if (timers.isEmpty())
            return null;
        var po = timers.get(0);
        return new TimerVo(po.getInitTime());
    }

}
