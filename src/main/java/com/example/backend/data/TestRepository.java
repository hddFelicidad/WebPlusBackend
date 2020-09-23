package com.example.backend.data;

import com.example.backend.po.TestPo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestPo, Long> {
    

}