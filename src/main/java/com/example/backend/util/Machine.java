package com.example.backend.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Deprecated()
public class Machine {
    private String id;
    private String name;
    private String machineId;
}