package com.example.backend.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.backend.dto.ScheduleInputDto;
import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.dto.TimeIntervalDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScheduleServiceTest {
        @Autowired
        ScheduleServiceImpl serviceImpl;

        @Test
        void syncSchedule() throws ParseException {
                ScheduleInputDto input = new ScheduleInputDto();
                List<ScheduleInputDto.Group> groups = new ArrayList<>();
                groups.add(new ScheduleInputDto.Group("5", "5组-童玲 (5)", 5, Arrays.asList(new TimeIntervalDto(7, 19))));
                groups.add(new ScheduleInputDto.Group("9", "9组-张敏（5）", 5, Arrays.asList(new TimeIntervalDto(7, 19))));
                groups.add(new ScheduleInputDto.Group("1", "1组-彭慧 (5)", 5,
                                Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
                groups.add(new ScheduleInputDto.Group("12", "12组-姚兰（5）", 5,
                                Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
                groups.add(new ScheduleInputDto.Group("15", "15组-李娟（5）", 5,
                                Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
                groups.add(new ScheduleInputDto.Group("3", "3组-李翠 (4)", 4, Arrays.asList(new TimeIntervalDto(7, 19))));
                groups.add(new ScheduleInputDto.Group("14", "14组-周  清（4）", 4,
                                Arrays.asList(new TimeIntervalDto(7, 19))));
                groups.add(new ScheduleInputDto.Group("16", "16组-朱美（4）", 4,
                                Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
                groups.add(new ScheduleInputDto.Group("40", "40组-高燕（5）", 5, Arrays.asList(new TimeIntervalDto(7, 19))));
                List<ScheduleInputDto.Machine> machines = new ArrayList<>();
                machines.add(new ScheduleInputDto.Machine("1", "line01", "1"));
                machines.add(new ScheduleInputDto.Machine("2", "line01", "1"));
                machines.add(new ScheduleInputDto.Machine("3", "line01", "1"));
                machines.add(new ScheduleInputDto.Machine("4", "line02", "2"));
                machines.add(new ScheduleInputDto.Machine("5", "line02", "2"));
                machines.add(new ScheduleInputDto.Machine("6", "line02", "2"));
                machines.add(new ScheduleInputDto.Machine("7", "line03", "3"));
                machines.add(new ScheduleInputDto.Machine("8", "line03", "3"));
                machines.add(new ScheduleInputDto.Machine("9", "line03", "3"));
                machines.add(new ScheduleInputDto.Machine("10", "line04", "4"));
                List<ScheduleInputDto.Order> orders = new ArrayList<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");

                input.setGroups(groups);
                input.setMachines(machines);
                input.setOrders(orders);
                orders.add(new ScheduleInputDto.Order("413095", "订单413095", false, 8, 8,
                                Arrays.asList("5", "9", "1", "12"), Arrays.asList("1", "2"),
                                dateFormat.parse("2020-11-4 10")));
                orders.add(new ScheduleInputDto.Order("414837", "订单414837", false, 8, 8,
                                Arrays.asList("3", "14", "16", "40"), Arrays.asList("2", "3"),
                                dateFormat.parse("2020-11-4 12")));
                orders.add(new ScheduleInputDto.Order("416153", "订单416153", false, 8, 11,
                                Arrays.asList("1", "15", "16", "40"), Arrays.asList("1", "3", "4"),
                                dateFormat.parse("2020-11-4 14")));
                serviceImpl.schedule(input, dateFormat.parse("2020-11-3 07"));
                ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
                System.out.println(output.getOrders().size());
        }
}