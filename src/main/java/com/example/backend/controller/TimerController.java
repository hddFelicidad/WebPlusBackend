package com.example.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.backend.vo.TimerVo;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = "/timer")
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