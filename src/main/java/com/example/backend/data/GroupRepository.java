package com.example.backend.data;

import com.example.backend.po.GroupPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<GroupPo, Integer> {

    GroupPo findGroupPoByGroupId(String groupId);
}
