package com.example.backend.util;

import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@PlanningEntity
public class SubOrder {
    String id;
    String orderId;
    Integer needHour;
    List<String> availableGroupIdList;
    List<String> availableMachineTypeIdList;
    Integer deadLineTimeGrain;

    @PlanningVariable(valueRangeProviderRefs = "groupRange")
    Group group;

    @PlanningVariable(valueRangeProviderRefs = "machineRange")
    Machine machine;

    @PlanningVariable(valueRangeProviderRefs = "timeGrainRange")
    Integer timeGrain;

    public SubOrder(String id, String orderId, Integer needHour, List<String> availableGroupIdList, List<String> availableMachineTypeIdList, Integer deadLineTimeGrain) {
        this.id = id;
        this.orderId = orderId;
        this.needHour = needHour;
        this.availableGroupIdList = availableGroupIdList;
        this.availableMachineTypeIdList = availableMachineTypeIdList;
        this.deadLineTimeGrain = deadLineTimeGrain;
    }
}