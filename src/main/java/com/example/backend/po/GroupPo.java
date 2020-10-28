package com.example.backend.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class GroupPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer groupId;

    String groupName;

    String leaderName;

    Integer memberCount;

    String className;
}
