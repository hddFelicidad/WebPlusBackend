package com.example.backend.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.example.backend.data.OrderSchduleRepository;
import com.example.backend.dto.ScheduleInputDto;
import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.dto.TimeIntervalDto;
import com.example.backend.po.OrderSchedulePo;
import com.example.backend.po.SubOrderSchedulePo;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.var;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    // 以4个小时为粒度划分子订单
    static final int subOrderMaxNeedTime = 4;

    @Autowired
    private OrderSchduleRepository orderScheduleRepository;

    @Autowired
    private SolverManager<SubOrderSchedule, UUID> solverManager;

    private ScheduleOutputDto solutionDto;

    private ScheduleInputDto currentInput;
    private SolverJob<SubOrderSchedule, UUID> solverJob;

    @PostConstruct
    private void initialize() {
        solutionDto = loadSolution();
    }

    @Scheduled(fixedRate = 3000)
    private void fixedRateJob() {
        tryGetScheduleOutput();
    }

    @Override
    public void schedule(ScheduleInputDto input) {
        currentInput = input;
        solverJob = null;
        solutionDto = null;
        scheduleInternal(input);
    }

    @Override
    public boolean scheduleInsertUrgentOrder(ScheduleInputDto input, Date insertTime, ScheduleInputDto.Order order) {
        // 如果没有上一次排程结果 直接失败
        // 可能是没有初始排程 或者正在排程
        if (solutionDto == null)
            return false;

        solverJob = null;
        solutionDto = null;
        // TODO: 重新排程以优先完成新订单
        // schedule(currentInput);
        return true;
    }

    @Override
    public ScheduleOutputDto tryGetScheduleOutput() {
        // 已有结果
        if (solutionDto != null)
            return solutionDto;
        // 当前没有排程任务
        if (solverJob == null)
            throw new RuntimeException("当前没有排程任务");
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
        solutionDto = createOutputDto(currentInput, solution);
        // 持久化
        saveSolution(solutionDto);
        return solutionDto;
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
        Date startTime = input.getStartTime();
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);

        // Time grain
        List<Integer> timeGrains = calculateTimeGrains(input, orders);

        // 划分子订单
        List<SubOrder> subOrders = new ArrayList<>();
        for (ScheduleInputDto.Order order : orders)
            subOrders.addAll(splitOrder(order, startTime, subOrderMaxNeedTime));

        // 排程
        SubOrderSchedule schedule = new SubOrderSchedule(startTimeCalendar.get(Calendar.HOUR_OF_DAY), groups, machines,
                timeGrains, subOrders);

        UUID problemId = UUID.randomUUID();
        solverJob = solverManager.solve(problemId, schedule);
    }

    private List<Integer> calculateTimeGrains(ScheduleInputDto input, List<ScheduleInputDto.Order> orders) {
        // 以小时为单位
        // 任务安排的时间范围为最迟的ddl与开始时间差值的倍率
        int factor = 2;
        // TODO: 开始时间应当对齐时间粒度
        Date startTime = input.getStartTime();
        Date finalDeadline = startTime;
        for (ScheduleInputDto.Order order : orders)
            if (order.getDeadline().after(finalDeadline))
                finalDeadline = order.getDeadline();
        int availableTimeInHour = (int) ((finalDeadline.getTime() - startTime.getTime()) / 1000L / 60L / 60L) * factor;
        List<Integer> timeGrains = new ArrayList<>(availableTimeInHour);
        for (int i = 0; i < availableTimeInHour; i++)
            timeGrains.add(i);
        return timeGrains;
    }

    private List<SubOrder> splitOrder(ScheduleInputDto.Order order, Date startTime, int subOrderMaxNeedTime) {
        List<SubOrder> subOrders = new ArrayList<>();
        int suborderIndex = 0;
        Date deadline = order.getDeadline();
        int remainHours = order.getNeedHour();
        Integer deadlineTimeGrain = (int) ((deadline.getTime() - startTime.getTime()) / 1000L / 60L / 60L);
        while (remainHours > subOrderMaxNeedTime) {
            subOrders.add(new SubOrder(order.getId() + '_' + ++suborderIndex, order.getId(), subOrderMaxNeedTime,
                    order.getNeedMemberCount(), order.getAvailableGroupIdList(), order.getAvailableMachineTypeIdList(),
                    deadlineTimeGrain));
            remainHours -= subOrderMaxNeedTime;
        }
        subOrders.add(new SubOrder(order.getId() + '_' + ++suborderIndex, order.getId(), remainHours,
                order.getNeedMemberCount(), order.getAvailableGroupIdList(), order.getAvailableMachineTypeIdList(),
                deadlineTimeGrain));
        return subOrders;
    }

    private ScheduleOutputDto createOutputDto(ScheduleInputDto input, SubOrderSchedule solution) {
        // 把排程结果转换为Dto
        List<ScheduleOutputDto.Order> outputOrders = new ArrayList<>(input.getOrders().size());
        HashMap<String, ScheduleOutputDto.Order> orderMap = new HashMap<>();
        ScheduleOutputDto res = new ScheduleOutputDto(outputOrders);
        for (ScheduleInputDto.Order inputOrder : input.getOrders()) {
            String orderId = inputOrder.getId();
            List<ScheduleOutputDto.SubOrder> outputSubOrders = new ArrayList<>();
            ScheduleOutputDto.Order outputOrder = res.new Order(orderId, outputSubOrders);
            outputOrders.add(outputOrder);
            orderMap.put(orderId, outputOrder);
        }
        for (SubOrder subOrder : solution.getSubOrderList()) {
            String orderId = subOrder.getOrderId();
            ScheduleOutputDto.Order outputOrder = orderMap.get(orderId);
            Date subOrderStartTime = new Date(
                    input.getStartTime().getTime() + subOrder.getTimeGrain() * 60L * 60L * 1000L);
            ScheduleOutputDto.SubOrder outputSubOrder = res.new SubOrder(subOrder.getId(), subOrderStartTime,
                    subOrder.getNeedHour(), subOrder.getGroupIdList(), subOrder.getMachine().getId());
            outputOrder.getSubOrders().add(outputSubOrder);
        }
        return res;
    }

    private List<OrderSchedulePo> createOrderSchedulePo(ScheduleOutputDto outputDto) {
        List<OrderSchedulePo> res = new ArrayList<>(outputDto.getOrders().size());
        for (ScheduleOutputDto.Order order : outputDto.getOrders()) {
            List<SubOrderSchedulePo> subOrderPos = new ArrayList<>();
            OrderSchedulePo orderPo = new OrderSchedulePo(null, order.getId(), new HashSet<>(subOrderPos));
            for (ScheduleOutputDto.SubOrder subOrder : order.getSubOrders())
                subOrderPos.add(new SubOrderSchedulePo(null, subOrder.getId(), subOrder.getStartTime(),
                        subOrder.getDurationTimeInHour(), subOrder.getGroupIdList(), subOrder.getMachineId()));
            res.add(orderPo);
        }
        return res;
    }

    private ScheduleOutputDto createOutputDto(List<OrderSchedulePo> orderSchedulePos) {
        List<ScheduleOutputDto.Order> orders = new ArrayList<>();
        ScheduleOutputDto res = new ScheduleOutputDto(orders);
        for (OrderSchedulePo orderPo : orderSchedulePos) {
            List<ScheduleOutputDto.SubOrder> subOrders = new ArrayList<>();
            ScheduleOutputDto.Order order = res.new Order(orderPo.getOrderId(), subOrders);
            for (SubOrderSchedulePo subOrderPo : orderPo.getSubOrders())
                subOrders.add(res.new SubOrder(subOrderPo.getSubOrderId(), subOrderPo.getStartTime(),
                        subOrderPo.getDurationTimeInHour(), subOrderPo.getGroupIdList(), subOrderPo.getMachineId()));
            res.getOrders().add(order);
        }
        return res;
    }

    void saveSolution(ScheduleOutputDto output) {
        var orderSchedulePos = createOrderSchedulePo(output);
        orderScheduleRepository.deleteAll();
        orderScheduleRepository.saveAll(orderSchedulePos);
    }

    ScheduleOutputDto loadSolution() {
        var orderSchedulePos = orderScheduleRepository.findAll();
        if (orderSchedulePos.size() != 0)
            return createOutputDto(orderSchedulePos);
        return null;
    }
}