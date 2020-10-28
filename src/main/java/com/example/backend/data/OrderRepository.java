package com.example.backend.data;

import com.example.backend.po.OrderPo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderPo, Integer> {
}
