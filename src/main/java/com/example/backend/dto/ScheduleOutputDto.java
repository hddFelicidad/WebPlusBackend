package com.example.backend.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleOutputDto {
    private List<Order> orders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Order {
        private String id;
        private String name;
        private List<SubOrder> subOrders;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class SubOrder {
        private String id;
        /**
         * 子订单开始的时间
         */
        private Date startTime;
        /**
         * 子订单持续的时间 以小时为单位
         */
        private Integer durationTimeInHour;
        /**
         * 处理该子订单的各个小组的Id
         */
        private List<String> groupIdList;
        /**
         * 处理该子订单时所使用的机器Id
         */
        private String machineId;
    }
}