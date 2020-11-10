package com.example.backend.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend.service.TimerService;
import com.example.backend.vo.TimerVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = "/timer")
@Api(value = "时间管理类")
public class TimerController {
    @Autowired
    TimerService service;

    @PutMapping()
    public void put(@RequestBody TimerVo entity) {
        service.updateTimer(entity);
    }

    @GetMapping()
    public TimerVo get() {
        return service.getTimer();
    }

}