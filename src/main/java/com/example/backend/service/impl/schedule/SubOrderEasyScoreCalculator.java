package com.example.backend.service.impl.schedule;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

public class SubOrderEasyScoreCalculator implements EasyScoreCalculator<SubOrderSchedule> {
    @Override
    public HardSoftScore calculateScore(SubOrderSchedule schedule) {
        int hardScore = 0;
        int softScore = 0;
        for (SubOrder a : schedule.getSubOrderList()) {
            // 设备不正确
            if (a.getMachine() != null && !a.getAvailableMachineTypeIdList().contains(a.getMachine().getMachineId()))
                hardScore--;
            // 用了重复的小组
            int selfOverlapCount = sameGroupCountOfSelf(a);
            hardScore -= selfOverlapCount;
            // 小组不正确
            if (a.getGroup1() != null && !a.getAvailableGroupIdList().contains(a.getGroup1().getId()))
                hardScore--;
            if (a.getGroup2() != null && !a.getAvailableGroupIdList().contains(a.getGroup2().getId()))
                hardScore--;
            if (a.getGroup3() != null && !a.getAvailableGroupIdList().contains(a.getGroup3().getId()))
                hardScore--;
            // 小组总人数不满足订单要求
            if (a.getTotalMemberCount() < a.getNeedMemberCount())
                hardScore -= a.getNeedMemberCount() - a.getTotalMemberCount();
            // 小组工作时间不符合
            if (a.getTimeGrain() != null) {
                int startHourOfDay = (schedule.getStartHourOfDay() + a.getTimeGrain()) % 24;
                if (!groupCanWork(a.getGroup1(), startHourOfDay, a))
                    hardScore--;
                if (!groupCanWork(a.getGroup2(), startHourOfDay, a))
                    hardScore--;
                if (!groupCanWork(a.getGroup3(), startHourOfDay, a))
                    hardScore--;
            }

            for (SubOrder b : schedule.getSubOrderList())
                // 时间交叉
                if (!a.getId().equals(b.getId()) && a.getTimeGrain() != null && b.getTimeGrain() != null
                        && a.getTimeGrain() <= b.getTimeGrain()
                        && a.getTimeGrain() + a.getNeedHour() > b.getTimeGrain()) {
                    // 占用相同的小组
                    int overlapGroupCount = sameGroupCount(a, b);
                    if (overlapGroupCount >= 1)
                        hardScore -= overlapGroupCount;
                    // 占用同一个设备
                    if (a.getMachine() != null && a.getMachine().equals(b.getMachine()))
                        hardScore--;
                }

            // 超过ddl
            if (a.getTimeGrain() != null && a.getTimeGrain() + a.getNeedHour() > a.getDeadLineTimeGrain())
                softScore--;
        }
        return HardSoftScore.of(hardScore, softScore);
    }

    private boolean groupCanWork(Group group, int startHourOfDay, SubOrder subOrder) {
        if (group == null)
            return true;
        return group.canWorkIn(startHourOfDay, subOrder.getNeedHour());
    }

    private int sameGroupCountOfSelf(SubOrder a) {
        int count = 0;
        List<Group> groupList = new ArrayList<>(4);
        if (a.getGroup1() != null)
            groupList.add(a.getGroup1());

        if (groupList.contains(a.getGroup2()))
            count++;
        if (a.getGroup2() != null)
            groupList.add(a.getGroup2());

        if (groupList.contains(a.getGroup3()))
            count++;
        if (a.getGroup3() != null)
            groupList.add(a.getGroup3());

        return count;
    }

    private int sameGroupCount(SubOrder a, SubOrder b) {
        int count = 0;
        List<Group> groupOfAList = new ArrayList<>(4);
        if (a.getGroup1() != null)
            groupOfAList.add(a.getGroup1());
        if (a.getGroup2() != null)
            groupOfAList.add(a.getGroup2());
        if (a.getGroup3() != null)
            groupOfAList.add(a.getGroup3());
        if (groupOfAList.contains(b.getGroup1()))
            count++;
        if (groupOfAList.contains(b.getGroup2()))
            count++;
        if (groupOfAList.contains(b.getGroup3()))
            count++;
        return count;
    }
}