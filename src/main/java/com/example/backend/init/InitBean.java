package com.example.backend.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitBean {
    @Autowired
    private InitSchedule schedule;

    @PostConstruct
    public void init(){
        // schedule.scheduleInit();
    }
}
