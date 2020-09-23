package com.example.backend.controller;

import java.util.List;

import com.example.backend.service.TestService;
import com.example.backend.vo.TestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(value = "test")
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping()
    public void postTest(@RequestBody TestVo vo) {
        testService.saveTests(vo);
    }

    @GetMapping()
    public List<TestVo> getMethodName() {
        return testService.getTests();
    }

}