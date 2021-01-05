package com.example.backend.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.example.backend.data.OrderScheduleRepository;
import com.example.backend.dto.ScheduleInputDto;
import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.dto.TimeIntervalDto;

import com.example.backend.po.OrderSchedulePo;
import com.example.backend.service.ScheduleInitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.var;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ScheduleServiceTest {
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
    static final TimeIntervalDto morningShift = new TimeIntervalDto(7, 8);
    static final TimeIntervalDto afternoonShift = new TimeIntervalDto(15, 8);
    static final TimeIntervalDto eveningShift = new TimeIntervalDto(23, 8);

    @Autowired
    ScheduleServiceImpl serviceImpl;
    @Autowired
    ScheduleInitService scheduleInitService;

    @Test
    void syncSchedule() throws ParseException {
        serviceImpl.schedule(genSimpleInput(), dateFormat.parse("2020-11-3 07"), 4, 0.8);
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
    }

    @Test
    void syncScheduleUrgentOrder() throws ParseException {
        var input = genSimpleInput();
        serviceImpl.schedule(input, dateFormat.parse("2020-11-3 07"), 4, 0.8);
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
        serviceImpl.scheduleInsertUrgentOrder(input, dateFormat.parse("2020-11-3 19"),
                new ScheduleInputDto.Order("urgent 1", "紧急订单 1", true, 5, 5,
                        new HashSet<>(Arrays.asList("g-M0", "g-M1", "g-M2", "g-M3", "g-A0", "g-A1", "g-A1", "g-A2")),
                        new HashSet<>(Arrays.asList("mt1", "mt3", "mt4")), dateFormat.parse("2020-11-4 12"), null),
                4, 0.8);
        output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
    }

    @Test
    void getActualInput() throws ParseException {
        ScheduleInputDto input = scheduleInitService.getScheduleInput();

        serviceImpl.schedule(input, dateFormat.parse("2018-11-01 07"));
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
    }

    private ScheduleInputDto genSimpleInput() throws ParseException {
        ScheduleInputDto input = new ScheduleInputDto();
        List<ScheduleInputDto.Group> groups = new ArrayList<>();
        groups.add(new ScheduleInputDto.Group("g-M0", "5组-童玲 (5)", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g-M1", "9组-张敏（5）", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g-M2", "9组-张敏（5）", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g-M3", "9组-张敏（5）", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g-A0", "1组-彭慧 (5)", 5, afternoonShift));
        groups.add(new ScheduleInputDto.Group("g-A1", "12组-姚兰（5）", 5, afternoonShift));
        groups.add(new ScheduleInputDto.Group("g-A2", "12组-姚兰（5）", 5, afternoonShift));
        groups.add(new ScheduleInputDto.Group("g-A3", "12组-姚兰（5）", 5, afternoonShift));
        groups.add(new ScheduleInputDto.Group("g-E0", "15组-李娟（5）", 5, eveningShift));
        groups.add(new ScheduleInputDto.Group("g-E1", "3组-李翠 (4)", 4, eveningShift));
        groups.add(new ScheduleInputDto.Group("g-E2", "3组-李翠 (4)", 4, eveningShift));
        groups.add(new ScheduleInputDto.Group("g-E3", "3组-李翠 (4)", 4, eveningShift));
        List<ScheduleInputDto.Machine> machines = new ArrayList<>();
        machines.add(new ScheduleInputDto.Machine("m1", "line01", "mt1"));
        machines.add(new ScheduleInputDto.Machine("m2", "line01", "mt1"));
        machines.add(new ScheduleInputDto.Machine("m3", "line01", "mt1"));
        machines.add(new ScheduleInputDto.Machine("m4", "line02", "mt2"));
        machines.add(new ScheduleInputDto.Machine("m5", "line02", "mt2"));
        machines.add(new ScheduleInputDto.Machine("m6", "line02", "mt2"));
        machines.add(new ScheduleInputDto.Machine("m7", "line03", "mt3"));
        machines.add(new ScheduleInputDto.Machine("m8", "line03", "mt3"));
        machines.add(new ScheduleInputDto.Machine("m9", "line03", "mt3"));
        machines.add(new ScheduleInputDto.Machine("m10", "line04", "mt4"));
        List<ScheduleInputDto.Order> orders = new ArrayList<>();

        input.setGroups(groups);
        input.setMachines(machines);
        input.setOrders(orders);
        orders.add(new ScheduleInputDto.Order("413095", "订单413095", false, 24, 8,
                new HashSet<>(Arrays.asList("g-M0", "g-M1", "g-M2", "g-M3", "g-A0", "g-A1", "g-A2", "g-A3")),
                new HashSet<>(Arrays.asList("mt1", "mt2")), dateFormat.parse("2020-11-5 10"), null));
        orders.add(new ScheduleInputDto.Order("414837", "订单414837", false, 36, 8,
                new HashSet<>(Arrays.asList("g-A0", "g-A1", "g-A2", "g-A3", "g-E0", "g-E1", "g-E2", "g-E3")),
                new HashSet<>(Arrays.asList("mt2", "mt3")), dateFormat.parse("2020-11-5 12"), null));
        orders.add(new ScheduleInputDto.Order("416153", "订单416153", false, 48, 9,
                new HashSet<>(Arrays.asList("g-E0", "g-E1", "g-E2", "g-E3", "g-M0", "g-M1", "g-M2", "g-M3")),
                new HashSet<>(Arrays.asList("mt1", "mt3", "mt4")), dateFormat.parse("2020-11-5 14"), null));
        return input;
    }

    @Test
    void loadSolutionTest() {
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
    }

    @Test
    void mockLoadSolutionTest() {
        OrderScheduleRepository mockOSR = mock(OrderScheduleRepository.class);
        List<OrderSchedulePo> orderSchedulePos = new ArrayList<>();
        when(mockOSR.findAll()).thenReturn(orderSchedulePos);
        assert (serviceImpl.loadSolution() != null);

    }

    @Test
    void scheduleTest() throws ParseException {
        ScheduleInputDto input = new ScheduleInputDto();
        List<ScheduleInputDto.Group> groups = new ArrayList<>();
        groups.add(new ScheduleInputDto.Group("g1", "5组-童玲 (5)", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g2", "9组-张敏（5）", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g3", "1组-彭慧 (5)", 5, eveningShift));
        groups.add(new ScheduleInputDto.Group("g4", "12组-姚兰（5）", 5, eveningShift));
        List<ScheduleInputDto.Machine> machines = new ArrayList<>();
        machines.add(new ScheduleInputDto.Machine("m1", "line01", "mt1"));
        machines.add(new ScheduleInputDto.Machine("m2", "line01", "mt1"));
        machines.add(new ScheduleInputDto.Machine("m3", "line01", "mt1"));
        machines.add(new ScheduleInputDto.Machine("m4", "line02", "mt2"));
        machines.add(new ScheduleInputDto.Machine("m5", "line02", "mt2"));
        machines.add(new ScheduleInputDto.Machine("m6", "line03", "mt3"));
        List<ScheduleInputDto.Order> orders = new ArrayList<>();
        input.setGroups(groups);
        input.setMachines(machines);
        input.setOrders(orders);
        orders.add(new ScheduleInputDto.Order("413095", "订单413095", false, 12, 8,
                new HashSet<>(Arrays.asList("g1", "g2", "g3", "g4")), new HashSet<>(Arrays.asList("mt1", "mt2")),
                dateFormat.parse("2020-11-4 14"), null));
        orders.add(
                new ScheduleInputDto.Order("414837", "订单414837", false, 12, 5, new HashSet<>(Arrays.asList("g1", "g4")),
                        new HashSet<>(Arrays.asList("mt2", "mt3")), dateFormat.parse("2020-11-4 12"), null));
        orders.add(new ScheduleInputDto.Order("416153", "订单416153", false, 15, 5,
                new HashSet<>(Arrays.asList("g2", "g3", "g4")), new HashSet<>(Arrays.asList("mt1", "mt3")),
                dateFormat.parse("2020-11-5 14"), null));

        serviceImpl.schedule(input, dateFormat.parse("2020-11-4 07"));
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);

    }

    @Test
    void scheduleTestNight() throws ParseException {
        ScheduleInputDto input = new ScheduleInputDto();
        List<ScheduleInputDto.Group> groups = new ArrayList<>();
        groups.add(new ScheduleInputDto.Group("g1", "5组-童玲 (5)", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g2", "9组-张敏（5）", 5, morningShift));
        List<ScheduleInputDto.Machine> machines = new ArrayList<>();
        machines.add(new ScheduleInputDto.Machine("m1", "line01", "mt1"));
        machines.add(new ScheduleInputDto.Machine("m2", "line01", "mt1"));
        List<ScheduleInputDto.Order> orders = new ArrayList<>();
        input.setGroups(groups);
        input.setMachines(machines);
        input.setOrders(orders);
        orders.add(
                new ScheduleInputDto.Order("413095", "订单413095", false, 24, 4, new HashSet<>(Arrays.asList("g1", "g2")),
                        new HashSet<>(Arrays.asList("mt1")), dateFormat.parse("2020-11-10 10"), null));
        serviceImpl.schedule(input, dateFormat.parse("2020-11-9 07"));
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);

    }

    @Test
    void averageLoadTest() throws ParseException {
        ScheduleInputDto input = new ScheduleInputDto();
        List<ScheduleInputDto.Group> groups = new ArrayList<>();
        groups.add(new ScheduleInputDto.Group("g1", "5组-童玲 (5)", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g2", "9组-张敏（5）", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g3", "10组-李敏（5）", 5, morningShift));
        List<ScheduleInputDto.Machine> machines = new ArrayList<>();
        machines.add(new ScheduleInputDto.Machine("m1", "line01", "mt1"));
        List<ScheduleInputDto.Order> orders = new ArrayList<>();
        input.setGroups(groups);
        input.setMachines(machines);
        input.setOrders(orders);
        orders.add(new ScheduleInputDto.Order("413095", "订单413095", false, 12, 4,
                new HashSet<>(Arrays.asList("g1", "g2", "g3")), new HashSet<>(Arrays.asList("mt1")),
                dateFormat.parse("2020-12-10 10"), null));
        serviceImpl.schedule(input, dateFormat.parse("2020-11-9 07"), 4, 2);
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
    }

    
    @Test
    void stageTest() throws ParseException {
        ScheduleInputDto input = new ScheduleInputDto();
        List<ScheduleInputDto.Group> groups = new ArrayList<>();
        groups.add(new ScheduleInputDto.Group("g1", "5组-童玲 (5)", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g2", "9组-张敏（5）", 5, morningShift));
        List<ScheduleInputDto.Machine> machines = new ArrayList<>();
        machines.add(new ScheduleInputDto.Machine("m1", "line01", "mt1"));
        machines.add(new ScheduleInputDto.Machine("m2", "line01", "mt1"));
        List<ScheduleInputDto.Order> orders = new ArrayList<>();
        input.setGroups(groups);
        input.setMachines(machines);
        input.setOrders(orders);
        orders.add(new ScheduleInputDto.Order("order1 装配", "订单413095 装配", false, 12, 4,
                new HashSet<>(Arrays.asList("g1", "g2")), new HashSet<>(Arrays.asList("mt1")),
                dateFormat.parse("2020-12-10 10"), null));
        orders.add(new ScheduleInputDto.Order("order2 测试", "订单413095 测试", false, 12, 4,
                new HashSet<>(Arrays.asList("g1", "g2")), new HashSet<>(Arrays.asList("mt1")),
                dateFormat.parse("2020-12-10 10"), "order1 装配"));
        serviceImpl.schedule(input, dateFormat.parse("2020-11-9 07"), 4, 2);
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
    }

}
