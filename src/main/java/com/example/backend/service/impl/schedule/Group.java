package com.example.backend.service.impl.schedule;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private String id;
    private String name;
    private Integer memberCount;
    /**
     * 一天之内的所有工作时间段
     */
    private List<TimeInterval> workIntervals;

    public boolean canWork(boolean morning) {
        boolean canMorning = workIntervals.size() == 1;
        if (canMorning)
            return morning;
        return !morning;
    }

    @Override
    public String toString() {
        return "{ " + id + ", " + memberCount.toString() + ", " + workIntervals.toString() + " }";
    }
}
