package com.example.backend.service.impl.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeInterval {
    /**
     * 起始时间 精确到小时 取值为0-23 闭区间
     */
    private Integer startHourOfDay;
    /**
     * 终止时间 精确到小时 取值为1-24 开区间
     */
    private Integer endHourOfDay;

    public boolean contains(TimeInterval interval) {
        return startHourOfDay <= interval.startHourOfDay && interval.endHourOfDay <= endHourOfDay;
    }
}
