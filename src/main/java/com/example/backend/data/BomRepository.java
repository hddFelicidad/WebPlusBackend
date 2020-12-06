package com.example.backend.data;

import com.example.backend.po.BomPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BomRepository extends JpaRepository<BomPo, Integer> {

    List<BomPo> findBomPosByBomId(String bomId);
}
