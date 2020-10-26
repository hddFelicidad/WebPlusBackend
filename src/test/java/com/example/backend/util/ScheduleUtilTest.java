package com.example.backend.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScheduleUtilTest {
    @Autowired
    ScheduleUtil util;

    @Test
    void testSimple() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group("1", "组一", 5, Arrays.asList(new TimeInterval(7, 19))));
        groups.add(new Group("2", "组二", 5, Arrays.asList(new TimeInterval(7, 19))));
        groups.add(new Group("3", "组三", 5, Arrays.asList(new TimeInterval(19, 24), new TimeInterval(0, 7))));
        groups.add(new Group("4", "组四", 5, Arrays.asList(new TimeInterval(19, 24), new TimeInterval(0, 7))));
        groups.add(new Group("5", "组五", 5, Arrays.asList(new TimeInterval(19, 24), new TimeInterval(0, 7))));
        groups.add(new Group("6", "组六", 6, Arrays.asList(new TimeInterval(7, 19))));
        groups.add(new Group("7", "组七", 6, Arrays.asList(new TimeInterval(7, 19))));
        groups.add(new Group("8", "组八", 5, Arrays.asList(new TimeInterval(19, 24), new TimeInterval(0, 7))));
        groups.add(new Group("9", "组九", 5, Arrays.asList(new TimeInterval(7, 19))));
        List<Machine> machines = new ArrayList<>();
        machines.add(new Machine("1", "line1", "1"));
        machines.add(new Machine("2", "line1", "1"));
        machines.add(new Machine("3", "line1", "1"));
        machines.add(new Machine("4", "line2", "2"));
        machines.add(new Machine("5", "line2", "2"));
        machines.add(new Machine("6", "line3", "3"));
        machines.add(new Machine("7", "line3", "3"));
        machines.add(new Machine("8", "line3", "3"));
        machines.add(new Machine("9", "line3", "3"));
        machines.add(new Machine("10", "line4", "4"));
        List<ScheduleInputData.Order> orders = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            ScheduleInputData input = new ScheduleInputData(dateFormat.parse("2020-10-26 09"), groups, machines,
                    orders);
            orders.add(input.new Order("1", "订单一", 20, Arrays.asList("1", "2", "3", "4"), Arrays.asList("1", "2"),
                    dateFormat.parse("2020-10-26 14")));
            orders.add(input.new Order("2", "订单二", 25, Arrays.asList("6", "7", "8", "9"), Arrays.asList("2", "3"),
                    dateFormat.parse("2020-10-26 14")));
            orders.add(input.new Order("3", "订单三", 19, Arrays.asList("3", "5", "8", "9"), Arrays.asList("1", "3", "4"),
                    dateFormat.parse("2020-10-26 14")));
            ScheduleOutputData output = util.solve(input);
            System.out.println(output.getOrders().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}