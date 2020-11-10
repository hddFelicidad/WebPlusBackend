package com.example.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend.service.TimerService;
import com.example.backend.vo.TimerVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping(value = "/timer")

public class TimerController {
    @Autowired
    TimerService service;

    @PostMapping()
    public void put(@RequestBody TimerVo entity) {
        service.updateTimer(entity);
    }

    @GetMapping()
    public TimerVo get() {
        return service.getTimer();
    }

}