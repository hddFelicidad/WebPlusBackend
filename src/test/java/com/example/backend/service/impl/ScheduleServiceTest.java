package com.example.backend.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.example.backend.data.OrderSchduleRepository;
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
    static final List<TimeIntervalDto> morningShift = Arrays.asList(new TimeIntervalDto(7, 19));
    static final List<TimeIntervalDto> eveningShift = Arrays.asList(new TimeIntervalDto(19, 24),
            new TimeIntervalDto(0, 7));

    @Autowired
    ScheduleServiceImpl serviceImpl;
    @Autowired
    ScheduleInitService scheduleInitService;

    @Test
    void syncSchedule() throws ParseException {
        serviceImpl.schedule(genSimpleInput(), dateFormat.parse("2020-11-3 07"));
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
    }

    @Test
    void syncScheduleUrgentOrder() throws ParseException {
        var input = genSimpleInput();
        serviceImpl.schedule(input, dateFormat.parse("2020-11-3 07"));
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
        serviceImpl.scheduleInsertUrgentOrder(input, dateFormat.parse("2020-11-3 19"),
                new ScheduleInputDto.Order("urgent 1", "紧急订单 1", true, 5, 5,
                        new HashSet<>(Arrays.asList("g1", "g15", "g16", "g40")),
                        new HashSet<>(Arrays.asList("mt1", "mt3", "mt4")), dateFormat.parse("2020-11-4 12")));
        output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
    }

    @Test
    void getActualInput() throws ParseException {
        ScheduleInputDto input = scheduleInitService.getScheduleInput();

        serviceImpl.schedule(input, dateFormat.parse("2017-11-3 07"));
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
    }

    private ScheduleInputDto genSimpleInput() throws ParseException {
        ScheduleInputDto input = new ScheduleInputDto();
        List<ScheduleInputDto.Group> groups = new ArrayList<>();
        groups.add(new ScheduleInputDto.Group("g5", "5组-童玲 (5)", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g9", "9组-张敏（5）", 5, morningShift));
        groups.add(new ScheduleInputDto.Group("g1", "1组-彭慧 (5)", 5, eveningShift));
        groups.add(new ScheduleInputDto.Group("g12", "12组-姚兰（5）", 5, eveningShift));
        groups.add(new ScheduleInputDto.Group("g15", "15组-李娟（5）", 5, eveningShift));
        groups.add(new ScheduleInputDto.Group("g3", "3组-李翠 (4)", 4, morningShift));
        groups.add(new ScheduleInputDto.Group("g14", "14组-周  清（4）", 4, morningShift));
        groups.add(new ScheduleInputDto.Group("g16", "16组-朱美（4）", 4, eveningShift));
        groups.add(new ScheduleInputDto.Group("g40", "40组-高燕（5）", 5, morningShift));
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
                new HashSet<>(Arrays.asList("g5", "g9", "g1", "g12")), new HashSet<>(Arrays.asList("mt1", "mt2")),
                dateFormat.parse("2020-11-5 10")));
        orders.add(new ScheduleInputDto.Order("414837", "订单414837", false, 36, 8,
                new HashSet<>(Arrays.asList("g3", "g14", "g16", "g40")), new HashSet<>(Arrays.asList("mt2", "mt3")),
                dateFormat.parse("2020-11-5 12")));
        orders.add(new ScheduleInputDto.Order("416153", "订单416153", false, 48, 10,
                new HashSet<>(Arrays.asList("g1", "g15", "g16", "g40")),
                new HashSet<>(Arrays.asList("mt1", "mt3", "mt4")), dateFormat.parse("2020-11-5 14")));
        return input;
    }

    @Test
    void loadSolutionTest() {
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);
    }

    @Test
    void mockLoadSolutionTest() {
        OrderSchduleRepository mockOSR = mock(OrderSchduleRepository.class);
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
                dateFormat.parse("2020-11-4 14")));
        orders.add(
                new ScheduleInputDto.Order("414837", "订单414837", false, 12, 5, new HashSet<>(Arrays.asList("g1", "g4")),
                        new HashSet<>(Arrays.asList("mt2", "mt3")), dateFormat.parse("2020-11-4 12")));
        orders.add(new ScheduleInputDto.Order("416153", "订单416153", false, 15, 5,
                new HashSet<>(Arrays.asList("g2", "g3", "g4")), new HashSet<>(Arrays.asList("mt1", "mt3")),
                dateFormat.parse("2020-11-5 14")));

        serviceImpl.schedule(input, dateFormat.parse("2020-11-4 07"));
        ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
        assert (output.getOrders().size() != 0);

    }
}
