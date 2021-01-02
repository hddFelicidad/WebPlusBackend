package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述一天之内的一个时间段，前闭后开
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeIntervalDto {
    /**
     * 起始时间 精确到小时 取值为0-23 闭区间
     */
    private Integer startHourOfDay;
    /**
     * 持续的时间 精确到小时 取值为1-24
     */
    private Integer lastTime;
}