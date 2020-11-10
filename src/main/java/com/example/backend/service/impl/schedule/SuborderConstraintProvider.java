package com.example.backend.service.impl.schedule;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class SuborderConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] { machineNotRight(constraintFactory), groupNotRight(constraintFactory),
                useSameGroup(constraintFactory), groupMemberCountNotEnough(constraintFactory),
                groupCannotWork(constraintFactory), groupConflict(constraintFactory),
                machineConflict(constraintFactory), ddlExceedUrgent(constraintFactory), ddlExceed(constraintFactory) };
    }

    private Constraint machineNotRight(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).filter(SubOrder::machineNotRight).penalize("machineNotRight",
                HardSoftScore.ONE_HARD);
    }

    private Constraint groupNotRight(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).penalize("groupNotRight", HardSoftScore.ONE_HARD,
                SubOrder::getGroupNotRightCount);
    }

    private Constraint useSameGroup(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).filter(SubOrder::useSameGroup).penalize("useSameGroup",
                HardSoftScore.ONE_HARD);
    }

    private Constraint groupMemberCountNotEnough(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).filter(SubOrder::memberCountNotEnough)
                .penalize("groupMemberCountNotEnough", HardSoftScore.ONE_HARD);
    }

    private Constraint groupCannotWork(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).penalize("groupCannotWork", HardSoftScore.ONE_HARD,
                SubOrder::groupCannotWorkCount);
    }

    private Constraint groupConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class)
                .join(SubOrder.class, Joiners.lessThan(SubOrder::getId), Joiners.equal(SubOrder::getTimeSlot))
                .filter(SubOrder::groupConflict).penalize("groupConflict", HardSoftScore.ONE_HARD);
    }

    private Constraint machineConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .from(SubOrder.class).join(SubOrder.class, Joiners.lessThan(SubOrder::getId),
                        Joiners.equal(SubOrder::getTimeSlot), Joiners.equal(SubOrder::getMachine))
                .penalize("machineConflict", HardSoftScore.ONE_HARD);
    }

    private Constraint ddlExceedUrgent(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).filter(s -> s.getUrgent() && s.ddlExceed())
                .penalize("ddlExceedUrgent", HardSoftScore.ONE_HARD);
    }

    private Constraint ddlExceed(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).filter(SubOrder::ddlExceed).penalize("ddlExceed",
                HardSoftScore.ONE_SOFT);
    }
}