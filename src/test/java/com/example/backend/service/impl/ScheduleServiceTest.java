package com.example.backend.service.impl;

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
    void syncSchedule() {
        ScheduleInputDto input = new ScheduleInputDto();
        List<ScheduleInputDto.Group> groups = new ArrayList<>();
        groups.add(new ScheduleInputDto.Group("1", "组一", 5, Arrays.asList(new TimeIntervalDto(7, 19))));
        groups.add(new ScheduleInputDto.Group("2", "组二", 5, Arrays.asList(new TimeIntervalDto(7, 19))));
        groups.add(
                new ScheduleInputDto.Group("3", "组三", 5, Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
        groups.add(
                new ScheduleInputDto.Group("4", "组四", 5, Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
        groups.add(
                new ScheduleInputDto.Group("5", "组五", 5, Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
        groups.add(new ScheduleInputDto.Group("6", "组六", 6, Arrays.asList(new TimeIntervalDto(7, 19))));
        groups.add(new ScheduleInputDto.Group("7", "组七", 6, Arrays.asList(new TimeIntervalDto(7, 19))));
        groups.add(
                new ScheduleInputDto.Group("8", "组八", 5, Arrays.asList(new TimeIntervalDto(19, 24), new TimeIntervalDto(0, 7))));
        groups.add(new ScheduleInputDto.Group("9", "组九", 5, Arrays.asList(new TimeIntervalDto(7, 19))));
        List<ScheduleInputDto.Machine> machines = new ArrayList<>();
        machines.add(new ScheduleInputDto.Machine("1", "line1", "1"));
        machines.add(new ScheduleInputDto.Machine("2", "line1", "1"));
        machines.add(new ScheduleInputDto.Machine("3", "line1", "1"));
        machines.add(new ScheduleInputDto.Machine("4", "line2", "2"));
        machines.add(new ScheduleInputDto.Machine("5", "line2", "2"));
        machines.add(new ScheduleInputDto.Machine("6", "line3", "3"));
        machines.add(new ScheduleInputDto.Machine("7", "line3", "3"));
        machines.add(new ScheduleInputDto.Machine("8", "line3", "3"));
        machines.add(new ScheduleInputDto.Machine("9", "line3", "3"));
        machines.add(new ScheduleInputDto.Machine("10", "line4", "4"));
        List<ScheduleInputDto.Order> orders = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            input.setStartTime(dateFormat.parse("2020-10-26 09"));
            input.setGroups(groups);
            input.setMachines(machines);
            input.setOrders(orders);
            orders.add(new ScheduleInputDto.Order("1", "订单一", 40, Arrays.asList("1", "2", "3", "4"), Arrays.asList("1", "2"),
                    dateFormat.parse("2020-10-27 09")));
            orders.add(new ScheduleInputDto.Order("2", "订单二", 24, Arrays.asList("6", "7", "8", "9"), Arrays.asList("2", "3"),
                    dateFormat.parse("2020-10-27 12")));
            orders.add(new ScheduleInputDto.Order("3", "订单三", 36, Arrays.asList("3", "5", "8", "9"), Arrays.asList("1", "3", "4"),
                    dateFormat.parse("2020-10-27 14")));
            serviceImpl.schedule(input);
            ScheduleOutputDto output = serviceImpl.waitForScheduleOutput();
            System.out.println(output.getOrders().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}