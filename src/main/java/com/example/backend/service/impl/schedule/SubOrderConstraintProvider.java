package com.example.backend.service.impl.schedule;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;
import org.optaplanner.core.impl.score.stream.uni.DefaultUniConstraintCollector;

public class SuborderConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] { machineNotRight(constraintFactory), groupNotRight(constraintFactory),
                useSameGroup(constraintFactory), groupMemberCountNotEnough(constraintFactory),
                groupCannotWork(constraintFactory), groupConflict(constraintFactory),
                machineConflict(constraintFactory), ddlExceedUrgent(constraintFactory), ddlExceed(constraintFactory),
                softMachineLoadBalance(constraintFactory), softGroupLoadBalance(constraintFactory) };
    }

    // 订单使用相应的机器
    private Constraint machineNotRight(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).filter(SubOrder::machineNotRight).penalize("machineNotRight",
                HardSoftScore.ONE_HARD);
    }

    // 订单使用相应的小组
    private Constraint groupNotRight(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).penalize("groupNotRight", HardSoftScore.ONE_HARD,
                SubOrder::getGroupNotRightCount);
    }

    // 排程结果小组不同
    private Constraint useSameGroup(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).filter(SubOrder::useSameGroup).penalize("useSameGroup",
                HardSoftScore.ONE_HARD);
    }

    // 小组成员数量要满足订单约束
    private Constraint groupMemberCountNotEnough(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).filter(SubOrder::memberCountNotEnough)
                .penalize("groupMemberCountNotEnough", HardSoftScore.ONE_HARD);
    }

    // 小组早晚班时间要符合
    private Constraint groupCannotWork(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).penalize("groupCannotWork", HardSoftScore.ONE_HARD,
                SubOrder::groupCannotWorkCount);
    }

    // 同一时间同一小组只能做一个订单
    private Constraint groupConflict(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class)
                .join(SubOrder.class, Joiners.lessThan(SubOrder::getId), Joiners.equal(SubOrder::getTimeSlot))
                .filter(SubOrder::groupConflict).penalize("groupConflict", HardSoftScore.ONE_HARD);
    }

    // 同一时间同一生产线只能做一个订单
    private Constraint machineConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .from(SubOrder.class).join(SubOrder.class, Joiners.lessThan(SubOrder::getId),
                        Joiners.equal(SubOrder::getTimeSlot), Joiners.equal(SubOrder::getMachine))
                .penalize("machineConflict", HardSoftScore.ONE_HARD);
    }

    // 插单不超时
    private Constraint ddlExceedUrgent(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).filter(s -> s.getUrgent() && s.ddlExceed())
                .penalize("ddlExceedUrgent", HardSoftScore.ONE_HARD);
    }

    // 不延期（软约束）
    private Constraint ddlExceed(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).filter(SubOrder::ddlExceed).penalize("ddlExceed",
                HardSoftScore.ONE_SOFT);
    }

    // 机器利用率最高
    private Constraint softMachineLoadBalance(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class).groupBy(machineLoadBalance(SubOrder::getMachine)).penalize(
                "softMachineLoadBalance", HardSoftScore.ONE_SOFT,
                MachineLoadBalanceData::getZeroDeviationSquaredSumRoot);
    }

    // 员工负载均衡
    private Constraint softGroupLoadBalance(ConstraintFactory constraintFactory) {
        return constraintFactory.from(SubOrder.class)
                .groupBy(grouprLoadBalance(SubOrder::getGroup1, SubOrder::getGroup2)).penalize("softGroupLoadBalance",
                        HardSoftScore.ONE_SOFT, GroupLoadBalanceData::getZeroDeviationSquaredSumRoot);
    }

    private static DefaultUniConstraintCollector<SubOrder, ?, MachineLoadBalanceData> machineLoadBalance(
            Function<SubOrder, Machine> getMachine) {
        return new DefaultUniConstraintCollector<>(MachineLoadBalanceData::new, (resultContainer, subOrder) -> {
            Machine dev = getMachine.apply(subOrder);
            return resultContainer.apply(dev);
        }, resultContainer -> resultContainer);
    }

    private static DefaultUniConstraintCollector<SubOrder, ?, GroupLoadBalanceData> grouprLoadBalance(
            Function<SubOrder, Group> getGroup1, Function<SubOrder, Group> getGroup2) {
        return new DefaultUniConstraintCollector<>(GroupLoadBalanceData::new, (resultContainer, subOrder) -> {
            Group a = getGroup1.apply(subOrder);
            Group b = getGroup2.apply(subOrder);
            return resultContainer.apply(a, b);
        }, resultContainer -> resultContainer);
    }

    private static final class MachineLoadBalanceData {
        private final Map<String, Long> countMap = new LinkedHashMap<>();
        private long squaredSum = 0L;

        private Runnable apply(Machine dev) {
            if (dev != null) {
                long count = countMap.compute(dev.getId(), (key, value) -> (value == null) ? 1L : value + 1L);
                squaredSum += 2 * count - 1;
            }
            return () -> {
                if (dev != null) {
                    Long count = countMap.compute(dev.getId(), (key, value) -> (value == 1L) ? null : value - 1L);
                    squaredSum -= count == null ? 1 : (2 * count + 1);
                }
            };
        }

        public int getZeroDeviationSquaredSumRoot() {
            return -(int) (Math.sqrt((double) squaredSum));
        }
    }

    private static final class GroupLoadBalanceData {
        private final Map<String, Long> countMap = new LinkedHashMap<>();
        private long squaredSum = 0L;

        private Runnable apply(Group a, Group b) {
            long deltaA = 0;
            if (a != null)
                deltaA += -1 + 2 * countMap.compute(a.getId(), (key, value) -> (value == null) ? 1L : value + 1L);
            if (b != null)
                deltaA += -1 + 2 * countMap.compute(b.getId(), (key, value) -> (value == null) ? 1L : value + 1L);
            squaredSum += deltaA;
            return () -> {
                long deltaB = 0;
                if (a != null)
                    deltaB += 1 + 2
                            * nullToZero(countMap.compute(a.getId(), (key, value) -> (value == 1) ? null : value - 1L));
                if (b != null)
                    deltaB += 1 + 2
                            * nullToZero(countMap.compute(b.getId(), (key, value) -> (value == 1) ? null : value - 1L));
                squaredSum -= deltaB;
            };
        }

        private long nullToZero(Long num) {
            return num == null ? 0L : num;
        }

        public int getZeroDeviationSquaredSumRoot() {
            return (int) (Math.sqrt((double) squaredSum));
        }
    }
}