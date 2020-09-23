package com.example.backend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.example.backend.data.TestRepository;
import com.example.backend.po.TestPo;
import com.example.backend.service.TestService;
import com.example.backend.vo.TestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    TestRepository repo;

    @Override
    public void saveTests(TestVo vo) {
        repo.save(new TestPo(null, vo.getName()));
    }

    @Override
    public List<TestVo> getTests() {
        return repo.findAll().stream().map(po -> new TestVo(po.getId(), po.getName())).collect(Collectors.toList());
    }
}