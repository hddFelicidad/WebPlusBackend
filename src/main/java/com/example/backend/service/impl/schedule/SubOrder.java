package com.example.backend.service.impl.schedule;

import java.util.ArrayList;
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
    private Integer needMemberCount;
    private List<String> availableGroupIdList;
    private List<String> availableMachineTypeIdList;
    private Integer deadLineTimeGrain;

    @PlanningVariable(valueRangeProviderRefs = "groupRange")
    private Group group1;
    @PlanningVariable(valueRangeProviderRefs = "groupRange", nullable = true)
    private Group group2;
    @PlanningVariable(valueRangeProviderRefs = "groupRange", nullable = true)
    private Group group3;

    @PlanningVariable(valueRangeProviderRefs = "machineRange")
    private Machine machine;

    @PlanningVariable(valueRangeProviderRefs = "timeGrainRange")
    private Integer timeGrain;

    public SubOrder(String id, String orderId, Integer needHour, Integer needMemberCount,
            List<String> availableGroupIdList, List<String> availableMachineTypeIdList, Integer deadLineTimeGrain) {
        this.id = id;
        this.orderId = orderId;
        this.needHour = needHour;
        this.needMemberCount = needMemberCount;
        this.availableGroupIdList = availableGroupIdList;
        this.availableMachineTypeIdList = availableMachineTypeIdList;
        this.deadLineTimeGrain = deadLineTimeGrain;
    }

    public List<String> getGroupIdList() {
        List<String> groupIdList = new ArrayList<>(4);
        if (group1 != null)
            groupIdList.add(group1.getId());
        if (group2 != null)
            groupIdList.add(group2.getId());
        if (group3 != null)
            groupIdList.add(group3.getId());
        return groupIdList;
    }

    public int getTotalMemberCount() {
        int count = 0;
        if (group1 != null)
            count += group1.getMemberCount();
        if (group2 != null)
            count += group2.getMemberCount();
        if (group3 != null)
            count += group3.getMemberCount();
        return count;
    }
}