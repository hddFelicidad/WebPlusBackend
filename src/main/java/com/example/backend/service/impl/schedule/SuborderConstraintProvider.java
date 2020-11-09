package com.example.backend.service.impl.schedule;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

public class SuborderConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] { machineNotRight(constraintFactory), groupNotRight(constraintFactory) };
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
        return constraintFactory.from(SubOrder.class).penalize("useSameGroup", HardSoftScore.ONE_HARD,
            SubOrder::getSameGroupCount);
    }
}