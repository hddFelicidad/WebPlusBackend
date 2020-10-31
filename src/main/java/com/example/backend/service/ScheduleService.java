package com.example.backend.service;

import com.example.backend.dto.ScheduleInputDto;
import com.example.backend.dto.ScheduleOutputDto;

public interface ScheduleService {
    /**
     * 提交排程的异步任务 该函数将会立刻返回 需要调用其他函数获取排程结果
     */
    void schedule(ScheduleInputDto input);

    /**
     * 尝试获取排程结果 该函数会立刻返回
     * @return 如果排程计算没有结束会返回null 否则返回排程结果
     */
    ScheduleOutputDto tryGetScheduleOutput();

    /**
     * 获取排程结果 如果排程计算没有结束将会阻塞直到计算结束
     * @return 排程结果
     */
    ScheduleOutputDto waitForScheduleOutput();
}