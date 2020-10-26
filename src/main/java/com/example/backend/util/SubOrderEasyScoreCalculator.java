package com.example.backend.util;

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
            // 小组不正确
            if (a.getGroup() != null && !a.getAvailableGroupIdList().contains(a.getGroup().getId()))
                hardScore--;
            // 小组工作时间不符合
            if (a.getGroup() != null && a.getTimeGrain() != null) {
                int startHourOfDay = (schedule.getStartHourOfDay() + a.getTimeGrain()) % 24;
                if (!a.getGroup().canWorkIn(startHourOfDay, a.getNeedHour()))
                    hardScore--;
            }

            for (SubOrder b : schedule.getSubOrderList())
                // 时间交叉
                if (!a.getId().equals(b.getId()) && a.getTimeGrain() != null && b.getTimeGrain() != null
                        && a.getTimeGrain() <= b.getTimeGrain()
                        && a.getTimeGrain() + a.getNeedHour() > b.getTimeGrain()) {
                    // 占用同一个小组
                    if (a.getGroup() != null && a.getGroup().equals(b.getGroup()))
                        hardScore--;
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
}