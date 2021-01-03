package com.example.backend.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInputDto {
    private List<Group> groups;
    private List<Machine> machines;
    private List<Order> orders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Group {
        private String id;
        private String name;
        private Integer memberCount;
        /**
         * 一天之内的所有工作时间段
         */
        private TimeIntervalDto workInterval;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Machine {
        private String id;
        private String name;
        private String machineId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Order {
        private String id;
        private String name;
        /**
         * 是否为紧急订单 也就是被中途插入的订单 不可延期
         */
        private Boolean urgent;
        /**
         * 完成整个订单需要的总时间
         */
        private Integer needHour;
        private Integer needMemberCount;
        /**
         * 可用的小组的Id列表
         */
        private HashSet<String> availableGroupIds;
        /**
         * 可用的机器的Id列表
         */
        private HashSet<String> availableMachineTypeIds;
        private Date deadline;
        /**
         * 在完成当前订单之前需要先完成其他的订单
         */
        private String requiredOrderId;
    }
}