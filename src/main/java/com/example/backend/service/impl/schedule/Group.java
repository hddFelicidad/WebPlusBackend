package com.example.backend.service.impl.schedule;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

    public boolean canWorkIn(int startHourOfDay, int durationHours) {
        while (startHourOfDay + durationHours > 24) {
            TimeInterval interval = new TimeInterval(startHourOfDay, 24);
            if (!canWorkIn(interval))
                return false;
            durationHours -= 24 - startHourOfDay;
            startHourOfDay = 0;
        }
        if (durationHours == 0)
            return true;
        return canWorkIn(new TimeInterval(startHourOfDay, startHourOfDay + durationHours));
    }

    public boolean canWorkIn(TimeInterval interval) {
        for (TimeInterval workInterval : workIntervals)
            if (workInterval.contains(interval))
                return true;
        return false;
    }
}
