package com.example.backend.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.optaplanner.core.api.solver.SolverJob;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduleUtil {
    @Autowired
    private SolverManager<SubOrderSchedule, UUID> solverManager;

    public ScheduleOutputData solve(ScheduleInputData data) {
        List<Group> groups = data.getGroups();
        List<Machine> machines = data.getMachines();
        List<ScheduleInputData.Order> orders = data.getOrders();

        // 任务安排的时间范围为最迟的ddl与开始时间差值的10倍 以小时为单位
        int factor = 10;
        Date startTime = data.getStartTime();
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);
        Date finalDeadline = startTime;
        int totalNeedTimeInHour = 0;
        for (ScheduleInputData.Order order : orders)
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
        for (ScheduleInputData.Order order : orders) {
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
        SubOrderSchedule schedule = new SubOrderSchedule(startTimeCalendar.get(Calendar.HOUR_OF_DAY), groups, machines, timeGrains, subOrders);
        schedule = solve(schedule);

        // 返回
        List<ScheduleOutputData.Order> outputOrders = new ArrayList<>(orders.size());
        HashMap<String, ScheduleOutputData.Order> orderMap = new HashMap<>();
        ScheduleOutputData res = new ScheduleOutputData(outputOrders);
        for (ScheduleInputData.Order inputOrder : orders) {
            String orderId = inputOrder.getId();
            String name = inputOrder.getName();
            List<ScheduleOutputData.SubOrder> outputSubOrders = new ArrayList<>();
            ScheduleOutputData.Order outputOrder = res.new Order(orderId, name, outputSubOrders);
            outputOrders.add(outputOrder);
            orderMap.put(orderId, outputOrder);
        }
        for (SubOrder subOrder : schedule.getSubOrderList()) {
            String orderId = subOrder.getOrderId();
            ScheduleOutputData.Order outputOrder = orderMap.get(orderId);
            Date subOrderStartTime = new Date(startTime.getTime() + subOrder.getTimeGrain() * 60L * 60L * 1000L);
            ScheduleOutputData.SubOrder outputSubOrder = res.new SubOrder(subOrder.getId(), subOrderStartTime, subOrder.getNeedHour(), subOrder.getGroup().getId(), subOrder.getMachine().getId());
            outputOrder.getSubOrders().add(outputSubOrder);
        }
        return res;
    }

    private SubOrderSchedule solve(SubOrderSchedule arrangement) {
        UUID problemId = UUID.randomUUID();
        SolverJob<SubOrderSchedule, UUID> solverJob = solverManager.solve(problemId, arrangement);
        SubOrderSchedule solution = null;
        try {
            solution = solverJob.getFinalBestSolution();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solution;
    }

}