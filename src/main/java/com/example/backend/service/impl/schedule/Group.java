package com.example.backend.service.impl.schedule;

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
    private Integer beginWorkHourInDay;
    private Integer workTime;

    public boolean canWork(int beginHourInDay, int lastTime) {
        if (beginWorkHourInDay <= beginHourInDay && beginHourInDay + lastTime <= beginWorkHourInDay + workTime)
            return true;
        beginHourInDay += 24;
        if (beginWorkHourInDay <= beginHourInDay && beginHourInDay + lastTime <= beginWorkHourInDay + workTime)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "{ " + id + ", " + memberCount.toString() + ", {" + beginWorkHourInDay + ", " + workTime + "} }";
    }
}
