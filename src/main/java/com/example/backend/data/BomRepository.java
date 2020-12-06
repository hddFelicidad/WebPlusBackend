package com.example.backend.data;

import com.example.backend.po.BomPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BomRepository extends JpaRepository<BomPo, Integer> {
}
