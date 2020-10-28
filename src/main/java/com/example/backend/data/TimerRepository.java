package com.example.backend.data;

import com.example.backend.po.TimerPo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends JpaRepository<TimerPo, Integer> {

}