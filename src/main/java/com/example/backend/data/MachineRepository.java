package com.example.backend.data;

import com.example.backend.po.MachinePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<MachinePo, Integer> {

    MachinePo findMachinePoByMachineId(int machineId);
}
