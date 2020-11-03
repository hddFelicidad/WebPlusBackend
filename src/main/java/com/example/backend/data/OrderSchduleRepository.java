package com.example.backend.data;

import com.example.backend.po.OrderSchedulePo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSchduleRepository extends JpaRepository<OrderSchedulePo, Integer> {
}