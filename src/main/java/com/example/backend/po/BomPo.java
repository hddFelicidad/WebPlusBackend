package com.example.backend.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BomPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String bomId;

    private String process;

    @ElementCollection
    private List<String> groupResourceList;

    @ElementCollection
    private List<String>  machineResourceList;

    private String changeTime;

    private String standardOutput;

    private int workerCount;
}
