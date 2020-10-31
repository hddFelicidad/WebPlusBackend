package com.example.backend.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.backend.dto.ScheduleInputDto;
import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.dto.TimeIntervalDto;
import com.example.backend.service.ScheduleService;
import com.example.backend.service.impl.schedule.Group;
import com.example.backend.service.impl.schedule.Machine;
import com.example.backend.service.impl.schedule.SubOrder;
import com.example.backend.service.impl.schedule.SubOrderSchedule;
import com.example.backend.service.impl.schedule.TimeInterval;

import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private SolverManager<SubOrderSchedule, UUID> solverManager;

    private ScheduleInputDto currentInput = null;
    private List<ScheduleInputDto.Order> urgentOrders;
    private List<Date> urgentOrderInsertTimes;
    private SolverJob<SubOrderSchedule, UUID> solverJob = null;
    // TODO: 持久化
    private ScheduleOutputDto solutionDto = null;

    @Override
    public void schedule(ScheduleInputDto input) {
        currentInput = input;
        urgentOrders = new ArrayList<>();
        urgentOrderInsertTimes = new ArrayList<>();
        solverJob = null;
        solutionDto = null;
        scheduleInternal(input);
    }

    @Override
    public ScheduleOutputDto tryGetScheduleOutput() {
        // 已经被返回过
        if (solutionDto != null)
            return solutionDto;
        // 已经解析完成
        if (solverJob.getSolverStatus() == SolverStatus.NOT_SOLVING)
            return waitForScheduleOutput();
        return null;
    }

    @Override
    public ScheduleOutputDto waitForScheduleOutput() {
        SubOrderSchedule solution = null;
        try {
            solution = solverJob.getFinalBestSolution();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 保存排程结果
        solutionDto = createDto(solution);
        // 持久化
        saveSolution();
        return solutionDto;
    }

    @Override
    public boolean scheduleInsertUrgentOrder(Date insertTime, com.example.backend.dto.ScheduleInputDto.Order order) {
        urgentOrders.add(order);
        urgentOrderInsertTimes.add(insertTime);
        // 如果上一次排程任务尚未结束 直接失败
        if (solverJob.getSolverStatus() != SolverStatus.NOT_SOLVING)
            return false;

        // 确保上一次排程的结果被保存在成员变量中
        waitForScheduleOutput();
        // TODO: 重新排程以优先完成新订单
        return true;
    }

    private TimeInterval createTimeInterval(TimeIntervalDto dto) {
        return new TimeInterval(dto.getStartHourOfDay(), dto.getEndHourOfDay());
    }

    private List<TimeInterval> createTimeIntervals(List<TimeIntervalDto> dtos) {
        return dtos.stream().map(dto -> createTimeInterval(dto)).collect(Collectors.toList());
    }

    private Group createGroup(ScheduleInputDto.Group dto) {
        return new Group(dto.getId(), dto.getName(), dto.getMemberCount(), createTimeIntervals(dto.getWorkIntervals()));
    }

    private List<Group> createGroups(List<ScheduleInputDto.Group> dtos) {
        return dtos.stream().map(dto -> createGroup(dto)).collect(Collectors.toList());
    }

    private Machine createMachine(ScheduleInputDto.Machine dto) {
        return new Machine(dto.getId(), dto.getName(), dto.getMachineId());
    }

    private List<Machine> createMachines(List<ScheduleInputDto.Machine> dtos) {
        return dtos.stream().map(dto -> createMachine(dto)).collect(Collectors.toList());
    }

    private void scheduleInternal(ScheduleInputDto input) {
        List<Group> groups = createGroups(input.getGroups());
        List<Machine> machines = createMachines(input.getMachines());
        List<ScheduleInputDto.Order> orders = input.getOrders();

        // 任务安排的时间范围为最迟的ddl与开始时间差值的10倍 以小时为单位
        int factor = 10;
        Date startTime = input.getStartTime();
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        Date finalDeadline = startTime;
        int totalNeedTimeInHour = 0;
        for (ScheduleInputDto.Order order : orders)
            if (order.getDeadline().after(finalDeadline)) {
                finalDeadline = order.getDeadline();
                totalNeedTimeInHour = order.getNeedHour();
            }
        int availableTimeInHour = (int) ((finalDeadline.getTime() - startTime.getTime()) / 1000L / 60L / 60L) * factor;
        List<Integer> timeGrains = new ArrayList<>(availableTimeInHour);
        for (int i = 0; i < availableTimeInHour; i++)
            timeGrains.add(i);

        // 以4个小时为粒度划分子订单
        int subOrderMaxNeedTime = 4;
        List<SubOrder> subOrders = new ArrayList<>(totalNeedTimeInHour / subOrderMaxNeedTime);
        for (ScheduleInputDto.Order order : orders) {
            int suborderIndex = 0;
            Date deadline = order.getDeadline();
            Integer deadlineTimeGrain = (int) ((deadline.getTime() - startTime.getTime()) / 1000L / 60L / 60L);
            while (order.getNeedHour() > subOrderMaxNeedTime) {
                subOrders.add(new SubOrder(order.getId() + '_' + ++suborderIndex, order.getId(), subOrderMaxNeedTime,
                        order.getAvailableGroupIdList(), order.getAvailableMachineTypeIdList(), deadlineTimeGrain));
                order.setNeedHour(order.getNeedHour() - subOrderMaxNeedTime);
            }
            subOrders.add(new SubOrder(order.getId() + '_' + ++suborderIndex, order.getId(), order.getNeedHour(),
                    order.getAvailableGroupIdList(), order.getAvailableMachineTypeIdList(), deadlineTimeGrain));
        }

        // 排程
        SubOrderSchedule schedule = new SubOrderSchedule(startTimeCalendar.get(Calendar.HOUR_OF_DAY), groups, machines,
                timeGrains, subOrders);

        UUID problemId = UUID.randomUUID();
        solverJob = solverManager.solve(problemId, schedule);
    }

    private ScheduleOutputDto createDto(SubOrderSchedule solution) {
        // 把排程结果转换为Dto
        List<ScheduleOutputDto.Order> outputOrders = new ArrayList<>(currentInput.getOrders().size());
        HashMap<String, ScheduleOutputDto.Order> orderMap = new HashMap<>();
        ScheduleOutputDto res = new ScheduleOutputDto(outputOrders);
        for (ScheduleInputDto.Order inputOrder : currentInput.getOrders()) {
            String orderId = inputOrder.getId();
            String name = inputOrder.getName();
            List<ScheduleOutputDto.SubOrder> outputSubOrders = new ArrayList<>();
            ScheduleOutputDto.Order outputOrder = res.new Order(orderId, name, outputSubOrders);
            outputOrders.add(outputOrder);
            orderMap.put(orderId, outputOrder);
        }
        for (SubOrder subOrder : solution.getSubOrderList()) {
            String orderId = subOrder.getOrderId();
            ScheduleOutputDto.Order outputOrder = orderMap.get(orderId);
            Date subOrderStartTime = new Date(
                    currentInput.getStartTime().getTime() + subOrder.getTimeGrain() * 60L * 60L * 1000L);
            ScheduleOutputDto.SubOrder outputSubOrder = res.new SubOrder(subOrder.getId(), subOrderStartTime,
                    subOrder.getNeedHour(), subOrder.getGroup().getId(), subOrder.getMachine().getId());
            outputOrder.getSubOrders().add(outputSubOrder);
        }
        return res;
    }

    void saveSolution() {
        // TODO: 保存当前的排程结果
    }
}