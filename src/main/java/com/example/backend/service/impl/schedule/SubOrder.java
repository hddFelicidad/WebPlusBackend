package com.example.backend.service.impl.schedule;

import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@PlanningEntity
public class SubOrder {
    private String id;
    private String orderId;
    private Integer needHour;
    private List<String> availableGroupIdList;
    private List<String> availableMachineTypeIdList;
    private Integer deadLineTimeGrain;

    @PlanningVariable(valueRangeProviderRefs = "groupRange")
    private Group group;

    @PlanningVariable(valueRangeProviderRefs = "machineRange")
    private Machine machine;

    @PlanningVariable(valueRangeProviderRefs = "timeGrainRange")
    private Integer timeGrain;

    public SubOrder(String id, String orderId, Integer needHour, List<String> availableGroupIdList,
            List<String> availableMachineTypeIdList, Integer deadLineTimeGrain) {
        this.id = id;
        this.orderId = orderId;
        this.needHour = needHour;
        this.availableGroupIdList = availableGroupIdList;
        this.availableMachineTypeIdList = availableMachineTypeIdList;
        this.deadLineTimeGrain = deadLineTimeGrain;
    }
}