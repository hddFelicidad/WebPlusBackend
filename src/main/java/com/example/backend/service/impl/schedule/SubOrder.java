package com.example.backend.service.impl.schedule;

import java.util.ArrayList;
import java.util.HashSet;
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
    private HashSet<String> availableGroupIds;
    private HashSet<String> availableMachineTypeIds;
    private Integer deadLineTimeGrain;

    @PlanningVariable(valueRangeProviderRefs = "groupRange")
    private Group group1;
    @PlanningVariable(valueRangeProviderRefs = "groupRange", nullable = true)
    private Group group2;
    @PlanningVariable(valueRangeProviderRefs = "groupRange", nullable = true)
    private Group group3;

    @PlanningVariable(valueRangeProviderRefs = "machineRange")
    private Machine machine;

    @PlanningVariable(valueRangeProviderRefs = "timeSlotRange")
    private TimeSlot timeSlot;

    public SubOrder(String id, String orderId, Integer needHour, Integer needMemberCount,
            HashSet<String> availableGroupIds, HashSet<String> availableMachineTypeIds, Integer deadLineTimeGrain) {
        this.id = id;
        this.orderId = orderId;
        this.needHour = needHour;
        this.needMemberCount = needMemberCount;
        this.availableGroupIds = availableGroupIds;
        this.availableMachineTypeIds = availableMachineTypeIds;
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

    public boolean machineNotRight() {
        return machine != null && !availableMachineTypeIds.contains(machine.getMachineId());
    }

    public int getGroupNotRightCount() {
        int res = 0;
        if (group1 != null && !availableGroupIds.contains(group1.getId()))
            res++;
        if (group2 != null && !availableGroupIds.contains(group2.getId()))
            res++;
        if (group3 != null && !availableGroupIds.contains(group3.getId()))
            res++;
        return res;
    }

    public int getSameGroupCount() {
        int res = 0;
        if (group2 != null && group2 == group1)
            res++;
        if (group3 != null && group3 == group1)
            res++;
        if (group3 != null && group3 == group2)
            res++;
        return res;
    }

    public int memberCountNotEnoughCount() {
        int count = 0;
        if (group1 != null)
            count += group1.getMemberCount();
        if (group2 != null)
            count += group2.getMemberCount();
        if (group3 != null)
            count += group3.getMemberCount();
        if (count < needMemberCount)
            return needMemberCount - count;
        return 0;
    }

    public int groupCannotWorkCount() {
        int count = 0;
        if (group1 != null && group1.canWorkIn(timeSlot.getTime().getHour(), needHour))
            count++;
        if (group2 != null && group2.canWorkIn(timeSlot.getTime().getHour(), needHour))
            count++;
        if (group3 != null && group3.canWorkIn(timeSlot.getTime().getHour(), needHour))
            count++;
        return count;
    }

    public boolean ddlExceed() {
        return timeSlot != null && deadLineTimeGrain < timeSlot.getIndex();
    }
}