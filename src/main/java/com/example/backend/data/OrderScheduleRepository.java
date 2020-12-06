package com.example.backend.data;

import com.example.backend.po.OrderSchedulePo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderScheduleRepository extends JpaRepository<OrderSchedulePo, Integer> {
}
