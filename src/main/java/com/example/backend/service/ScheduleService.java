package com.example.backend.service;

import java.util.Date;

import com.example.backend.dto.ScheduleInputDto;
import com.example.backend.dto.ScheduleOutputDto;

public interface ScheduleService {
    /**
     * 提交排程的异步任务 该函数将会立刻返回 需要调用其他函数获取排程结果
     */
    void schedule(ScheduleInputDto input);

    /**
     * 插入紧急订单重新提交排程的异步任务 该函数会立刻返回 需要调用其他函数获取排程结果 但是如果插单前整个排程任务（或者其他的插单排程任务）还没有完成
     * 该方法将会失败
     * 
     * @param insertTime 插单时间 此时间之前排好的不会被改变 此时间之后的排程可能会被改变来保证该紧急订单优先完成
     * @param order      紧急订单
     * @return 如果此时上一次排程任务还没有完成 将会返回false 否则为true
     */
    boolean scheduleInsertUrgentOrder(ScheduleInputDto input, Date insertTime, ScheduleInputDto.Order urgentOrder);

    /**
     * 尝试获取排程结果 该函数会立刻返回
     * 
     * @return 如果排程计算没有结束会返回null 否则返回排程结果
     */
    ScheduleOutputDto tryGetScheduleOutput();

    /**
     * 获取排程结果 如果排程计算没有结束将会阻塞直到计算结束
     * 
     * @return 排程结果
     */
    ScheduleOutputDto waitForScheduleOutput();
}