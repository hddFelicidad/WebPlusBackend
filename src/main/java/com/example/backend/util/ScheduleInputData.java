package com.example.backend.util;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInputData {
    Date startTime;
    List<Group> groups;
    List<Machine> machines;
    List<Order> orders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Order {
        String id;
        String name;
        /**
         * 完成整个订单需要的总时间
         */
        Integer needHour;
        /**
         * 可用的小组的Id列表
         */
        List<String> availableGroupIdList;
        /**
         * 可用的机器的Id列表
         */
        List<String> availableMachineTypeIdList;
        Date deadline;
    }
}