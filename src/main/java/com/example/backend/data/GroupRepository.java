package com.example.backend.data;

import com.example.backend.po.GroupPo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupPo, Integer> {

}
