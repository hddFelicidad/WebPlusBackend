package com.example.backend.util;

import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@PlanningSolution
public class SubOrderSchedule {
    @ValueRangeProvider(id = "groupRange")
    @ProblemFactCollectionProperty
    List<Group> groupList;

    @ValueRangeProvider(id = "machineRange")
    @ProblemFactCollectionProperty
    List<Machine> machineList;

    @ValueRangeProvider(id = "timeGrainRange")
    @ProblemFactCollectionProperty
    List<Integer> timeGrainList;

    @PlanningEntityCollectionProperty
    List<SubOrder> subOrderList;

    @PlanningScore
    HardSoftScore score;

    public SubOrderSchedule(List<Group> groupList, List<Machine> machineList, List<Integer> timeGrainList, List<SubOrder> subOrderList) {
        this.groupList = groupList;
        this.machineList = machineList;
        this.timeGrainList = timeGrainList;
        this.subOrderList = subOrderList;
    }

    public List<SubOrder> getSubOrderList() {
        return subOrderList;
    }

    public HardSoftScore getScore() {
        return score;
    }

}