package com.example.backend.service;

import java.util.List;

import com.example.backend.vo.TestVo;

public interface TestService {

    void saveTests(TestVo msg);

    List<TestVo> getTests();

}