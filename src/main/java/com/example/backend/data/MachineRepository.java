package com.example.backend.data;

import com.example.backend.po.MachinePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<MachinePo, Integer> {

    List<MachinePo> findMachinePosByMachineId(String machineId);

    List<MachinePo> findMachinePosByMachineName(String machineName);
}
