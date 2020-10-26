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
    private Date startTime;
    private List<Group> groups;
    private List<Machine> machines;
    private List<Order> orders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Order {
        private String id;
        private String name;
        /**
         * 完成整个订单需要的总时间
         */
        private Integer needHour;
        /**
         * 可用的小组的Id列表
         */
        private List<String> availableGroupIdList;
        /**
         * 可用的机器的Id列表
         */
        private List<String> availableMachineTypeIdList;
        private Date deadline;
    }
}