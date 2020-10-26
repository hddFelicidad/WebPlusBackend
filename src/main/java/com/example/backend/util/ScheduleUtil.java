package com.example.backend.util;

import java.util.UUID;

import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleUtil {
    @Autowired
    SolverManager<SubOrderSchedule, UUID> solverManager;

    public ScheduleOutputData solve(ScheduleInputData data) {
        // TODO:
        return null;
    }

    public SubOrderSchedule solve(SubOrderSchedule arrangement) {
        UUID problemId = UUID.randomUUID();
        SolverJob<SubOrderSchedule, UUID> solverJob = solverManager.solve(problemId, arrangement);
        SubOrderSchedule solution = null;
        try {
            solution = solverJob.getFinalBestSolution();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solution;
    }

}