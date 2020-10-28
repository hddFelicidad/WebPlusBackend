package com.example.backend.data;

import com.example.backend.po.MachinePo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<MachinePo, Integer> {
}
