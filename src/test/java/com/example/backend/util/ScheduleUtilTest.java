package com.example.backend.util;

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
        List<Group> groupList = new ArrayList<>();
        groupList.add(new Group("1", "组一", 5));
        groupList.add(new Group("2", "组二", 5));
        groupList.add(new Group("3", "组三", 5));
        groupList.add(new Group("4", "组四", 5));
        groupList.add(new Group("5", "组五", 5));
        groupList.add(new Group("6", "组六", 6));
        groupList.add(new Group("7", "组七", 6));
        groupList.add(new Group("8", "组八", 5));
        groupList.add(new Group("9", "组九", 5));
        List<Machine> machineList = new ArrayList<>();
        machineList.add(new Machine("1", "line1", "1"));
        machineList.add(new Machine("2", "line1", "1"));
        machineList.add(new Machine("3", "line1", "1"));
        machineList.add(new Machine("4", "line2", "2"));
        machineList.add(new Machine("5", "line2", "2"));
        machineList.add(new Machine("6", "line3", "3"));
        machineList.add(new Machine("7", "line3", "3"));
        machineList.add(new Machine("8", "line3", "3"));
        machineList.add(new Machine("9", "line3", "3"));
        machineList.add(new Machine("10", "line4", "4"));
        List<Integer> timeGrainList = new ArrayList<>();
        for (int i = 0; i < 50; i++)
            timeGrainList.add(i);
        List<SubOrder> subOrderList = new ArrayList<>();
        subOrderList.add(new SubOrder("1", "1", 10, Arrays.asList("1", "2", "3", "4"), Arrays.asList("1", "2")));
        subOrderList.add(new SubOrder("2", "1", 10, Arrays.asList("1", "2", "3", "4"), Arrays.asList("1", "2")));
        subOrderList.add(new SubOrder("3", "1", 10, Arrays.asList("1", "2", "3", "4"), Arrays.asList("1", "2")));
        subOrderList.add(new SubOrder("4", "1", 10, Arrays.asList("1", "2", "3", "4"), Arrays.asList("1", "2")));
        subOrderList.add(new SubOrder("5", "2", 15, Arrays.asList("6", "7", "8", "9"), Arrays.asList("2", "3")));
        subOrderList.add(new SubOrder("6", "2", 15, Arrays.asList("6", "7", "8", "9"), Arrays.asList("2", "3")));
        subOrderList.add(new SubOrder("7", "2", 15, Arrays.asList("6", "7", "8", "9"), Arrays.asList("2", "3")));
        subOrderList.add(new SubOrder("8", "3", 10, Arrays.asList("3", "5", "8", "9"), Arrays.asList("1", "3", "4")));
        subOrderList.add(new SubOrder("9", "3", 10, Arrays.asList("3", "5", "8", "9"), Arrays.asList("1", "3", "4")));
        subOrderList.add(new SubOrder("10", "3", 10, Arrays.asList("3", "5", "8", "9"), Arrays.asList("1", "3", "4")));
        subOrderList.add(new SubOrder("11", "3", 10, Arrays.asList("3", "5", "8", "9"), Arrays.asList("1", "3", "4")));
        SubOrderSchedule arrangement = new SubOrderSchedule(groupList, machineList, timeGrainList, subOrderList);
        arrangement = util.solve(arrangement);
        System.out.println(arrangement.subOrderList.size());
    }
}