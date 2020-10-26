package com.example.backend.util;

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
}