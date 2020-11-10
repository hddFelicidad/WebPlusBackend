package com.example.backend.service.impl;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.example.backend.service.impl.schedule.TimeSlot;

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
    static final int subOrderMaxNeedTime = 12;

    @Autowired
    private OrderSchduleRepository orderScheduleRepository;

    @Autowired
    private SolverManager<SubOrderSchedule, UUID> solverManager;

    private Map<String, List<ScheduleOutputDto.SubOrder>> fixedOrderSubOrders;
    private ScheduleOutputDto solutionDto;

    private ScheduleInputDto currentInput;
    private Date timeSlotstartTime;
    private SolverJob<SubOrderSchedule, UUID> solverJob;

    @PostConstruct
    private void initialize() {
        solutionDto = loadSolution();
    }

    @Scheduled(fixedRate = 3000)
    private void fixedRateJob() {
        if (solverJob != null)
            tryGetScheduleOutput();
    }

    @Override
    public void schedule(ScheduleInputDto input, Date startTime) {
        currentInput = input;
        timeSlotstartTime = startTime;
        solverJob = null;
        fixedOrderSubOrders = null;
        solutionDto = null;
        scheduleInternal(input, startTime);
    }

    @Override
    public boolean scheduleInsertUrgentOrder(ScheduleInputDto input, Date insertTime, ScheduleInputDto.Order order) {
        // 如果没有上一次排程结果 直接失败
        // 可能是没有初始排程 或者正在排程
        if (solutionDto == null)
            return false;

        currentInput = input;
        timeSlotstartTime = insertTime;
        solverJob = null;
        fixedOrderSubOrders = new HashMap<>();
        // 重新排程以优先完成新订单
        scheduleInsertUrgentOrderInternal(currentInput, solutionDto, insertTime, order);
        solutionDto = null;
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
        if (solutionDto != null) return solutionDto;
        SubOrderSchedule solution = null;
        try {
            solution = solverJob.getFinalBestSolution();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 保存排程结果
        solutionDto = createOutputDto(fixedOrderSubOrders, currentInput, timeSlotstartTime, solution);
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

    private void scheduleInternal(ScheduleInputDto input, Date startTime) {
        List<Group> groups = createGroups(input.getGroups());
        List<Machine> machines = createMachines(input.getMachines());
        List<ScheduleInputDto.Order> orders = input.getOrders();
        Calendar startTimeCalendar = Calendar.getInstance();
        startTimeCalendar.setTime(startTime);

        // Time grain
        List<TimeSlot> timeSlots = getTimeSlots(startTime, orders);

        // 划分子订单
        List<SubOrder> subOrders = new ArrayList<>();
        for (ScheduleInputDto.Order order : orders)
            subOrders.addAll(splitOrder(order, startTime, subOrderMaxNeedTime));

        // 排程
        SubOrderSchedule schedule = new SubOrderSchedule(startTimeCalendar.get(Calendar.HOUR_OF_DAY), groups, machines,
                timeSlots, subOrders);

        UUID problemId = UUID.randomUUID();
        solverJob = solverManager.solve(problemId, schedule);
    }

    private void scheduleInsertUrgentOrderInternal(ScheduleInputDto input, ScheduleOutputDto output, Date insertTime,
            ScheduleInputDto.Order urgentOrder) {
        Map<String, ScheduleInputDto.Order> inputOrderMap = input.getOrders().stream()
                .collect(Collectors.toMap(o -> o.getId(), o -> o));
        List<Group> groups = createGroups(input.getGroups());
        List<Machine> machines = createMachines(input.getMachines());
        Calendar insertTimeCalendar = Calendar.getInstance();
        insertTimeCalendar.setTime(insertTime);

        // Time grain
        // 需要考虑当前子订单全部完成
        List<ScheduleInputDto.Order> orders = input.getOrders();
        orders.add(urgentOrder);
        List<TimeSlot> timeSlots = getTimeSlots(insertTime, orders);

        // 需要排程的子订单 初始值先加入紧急子订单
        List<SubOrder> subOrders = splitOrder(urgentOrder, insertTime, subOrderMaxNeedTime);
        // 加入需要重新安排的原有的子订单
        for (var outputOrder : output.getOrders()) {
            ScheduleInputDto.Order inputOrder = inputOrderMap.get(outputOrder.getId());
            for (ScheduleOutputDto.SubOrder outputSubOrder : outputOrder.getSubOrders()) {
                if (outputSubOrder.getEndTime().after(insertTime))
                    // 需要重新安排
                    subOrders.add(new SubOrder(outputSubOrder.getId(), outputOrder.getId(), inputOrder.getUrgent(),
                            outputSubOrder.getDurationTimeInHour(), inputOrder.getNeedMemberCount(),
                            inputOrder.getAvailableGroupIds(), inputOrder.getAvailableMachineTypeIds(),
                            calculateTimeGrain(insertTime, inputOrder.getDeadline())));
                else {
                    // 保存不需要重排的子订单
                    List<ScheduleOutputDto.SubOrder> outputSubOrders;
                    if (fixedOrderSubOrders.containsKey(outputOrder.getId()))
                        outputSubOrders = fixedOrderSubOrders.get(outputOrder.getId());
                    else {
                        outputSubOrders = new ArrayList<>();
                        fixedOrderSubOrders.put(outputOrder.getId(), outputSubOrders);
                    }
                    outputSubOrders.add(outputSubOrder);
                }
            }
        }

        // 排程
        SubOrderSchedule schedule = new SubOrderSchedule(insertTimeCalendar.get(Calendar.HOUR_OF_DAY), groups, machines,
                timeSlots, subOrders);

        UUID problemId = UUID.randomUUID();
        solverJob = solverManager.solve(problemId, schedule);
    }

    private List<TimeSlot> getTimeSlots(Date startTime, List<ScheduleInputDto.Order> orders) {
        // 开始时间必须对齐时间粒度 或者简单地对齐7点与19点
        // 以小时为单位
        int totalTimeSlotHour = 0;
        for (ScheduleInputDto.Order order : orders)
            totalTimeSlotHour += order.getNeedHour() / subOrderMaxNeedTime + 1;
        totalTimeSlotHour *= 2;

        List<TimeSlot> timeSlots = new ArrayList<>(totalTimeSlotHour);
        for (int i = 0; i < totalTimeSlotHour; i++)
            timeSlots.add(new TimeSlot(i, new Date(startTime.getTime() + i * subOrderMaxNeedTime * 1000L * 60L * 60L)
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()));
        return timeSlots;
    }

    private List<SubOrder> splitOrder(ScheduleInputDto.Order order, Date startTime, int subOrderMaxNeedTime) {
        List<SubOrder> subOrders = new ArrayList<>();
        int suborderIndex = 0;
        Date deadline = order.getDeadline();
        int remainHours = order.getNeedHour();
        Integer deadlineTimeGrain = calculateTimeGrain(startTime, deadline);
        while (remainHours > subOrderMaxNeedTime) {
            subOrders.add(new SubOrder(order.getId() + '_' + ++suborderIndex, order.getId(), order.getUrgent(),
                    subOrderMaxNeedTime, order.getNeedMemberCount(), order.getAvailableGroupIds(),
                    order.getAvailableMachineTypeIds(), deadlineTimeGrain));
            remainHours -= subOrderMaxNeedTime;
        }
        subOrders.add(new SubOrder(order.getId() + '_' + ++suborderIndex, order.getId(), order.getUrgent(), remainHours,
                order.getNeedMemberCount(), order.getAvailableGroupIds(), order.getAvailableMachineTypeIds(),
                deadlineTimeGrain));
        return subOrders;
    }

    private Integer calculateTimeGrain(Date startTime, Date time) {
        return (int) ((time.getTime() - startTime.getTime()) / subOrderMaxNeedTime / 1000L / 60L / 60L);
    }

    private ScheduleOutputDto createOutputDto(Map<String, List<ScheduleOutputDto.SubOrder>> fixedSubOrders,
            ScheduleInputDto input, Date startTime, SubOrderSchedule solution) {
        // 把排程结果转换为Dto
        List<ScheduleOutputDto.Order> outputOrders = new ArrayList<>(input.getOrders().size());
        HashMap<String, ScheduleOutputDto.Order> orderMap = new HashMap<>();
        ScheduleOutputDto res = new ScheduleOutputDto(outputOrders);
        for (ScheduleInputDto.Order inputOrder : input.getOrders()) {
            String orderId = inputOrder.getId();
            ScheduleOutputDto.Order outputOrder = new ScheduleOutputDto.Order(orderId, new ArrayList<>());
            outputOrders.add(outputOrder);
            orderMap.put(orderId, outputOrder);
        }
        // 原本的固定的子订单
        if (fixedOrderSubOrders != null)
            for (var entry : fixedSubOrders.entrySet()) {
                String orderId = entry.getKey();
                ScheduleOutputDto.Order outputOrder = orderMap.get(orderId);
                outputOrder.getSubOrders().addAll(entry.getValue());
            }
        // 排好的子订单
        for (SubOrder subOrder : solution.getSubOrderList()) {
            String orderId = subOrder.getOrderId();
            ScheduleOutputDto.Order outputOrder = orderMap.get(orderId);
            Date subOrderStartTime = (subOrder.getTimeSlot() != null)
                    ? Date.from(subOrder.getTimeSlot().getTime().atZone(ZoneId.systemDefault()).toInstant())
                    : null;
            ScheduleOutputDto.SubOrder outputSubOrder = new ScheduleOutputDto.SubOrder(subOrder.getId(),
                    subOrderStartTime, subOrder.getNeedHour(), subOrder.getGroupIdList(),
                    (subOrder.getMachine() != null) ? subOrder.getMachine().getId() : null);
            outputOrder.getSubOrders().add(outputSubOrder);
        }
        return res;
    }

    private List<OrderSchedulePo> createOrderSchedulePo(ScheduleOutputDto outputDto) {
        List<OrderSchedulePo> res = new ArrayList<>(outputDto.getOrders().size());
        for (ScheduleOutputDto.Order order : outputDto.getOrders()) {
            HashSet<SubOrderSchedulePo> subOrderPos = new HashSet<>();
            OrderSchedulePo orderPo = new OrderSchedulePo(null, order.getId(), subOrderPos);
            for (ScheduleOutputDto.SubOrder subOrder : order.getSubOrders())
                subOrderPos.add(new SubOrderSchedulePo(null, subOrder.getId(),
                        subOrder.getStartTime() == null ? null : new Timestamp(subOrder.getStartTime().getTime()),
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
            ScheduleOutputDto.Order order = new ScheduleOutputDto.Order(orderPo.getOrderId(), subOrders);
            for (SubOrderSchedulePo subOrderPo : orderPo.getSubOrders())
                subOrders.add(new ScheduleOutputDto.SubOrder(subOrderPo.getSubOrderId(), subOrderPo.getStartTime(),
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