package com.example.backend.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend.vo.TimerVo;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = "/timer")
@Api(value = "时间管理类")
public class TimerController {
    @PutMapping()
    public void put(@RequestBody TimerVo entity) {
        // TODO: process PUT request
    }

    @GetMapping()
    public TimerVo get() {
        return null;
    }

}