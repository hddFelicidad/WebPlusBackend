package com.example.backend.service.impl;

import com.example.backend.data.TimerRepository;
import com.example.backend.po.TimerPo;
import com.example.backend.vo.TimerVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



@SpringBootTest
public class TimerServiceTest {

    @Autowired
    TimerServiceImpl timerService;

    @Test
    void updateTimerTest() {
        TimerVo vo=new TimerVo();
        vo.setInitTime(new Date());
        Timestamp ts=new Timestamp(1604912845);
        TimerPo tp=new TimerPo(1,ts);
        TimerRepository mockTimer =mock(TimerRepository.class);
        when(mockTimer.save(tp)).thenReturn(null);

        assertNull(mockTimer.save(tp));

    }

    @Test
    void getTimerTest(){
        TimerRepository mockTimer =mock(TimerRepository.class);
        when(mockTimer.findAll()).thenReturn(null);

        assertNull(timerService.getTimer());
    }

}
