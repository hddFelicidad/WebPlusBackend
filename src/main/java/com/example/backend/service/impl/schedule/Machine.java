package com.example.backend.service.impl.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Machine {
    private String id;
    private String name;
    private String machineId;

    @Override
    public String toString() {
        return "{ " + id + ", " + machineId + " }";
    }
}
