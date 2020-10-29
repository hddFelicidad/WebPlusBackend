package com.example.backend.data;

import com.example.backend.po.OrderPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderPo, Integer> {

    OrderPo findOrderPoByOrderId(String orderId);

}
