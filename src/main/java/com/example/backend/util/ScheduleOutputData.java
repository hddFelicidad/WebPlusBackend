package com.example.backend.util;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleOutputData {
    List<Order> orders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Order {
        String id;
        String name;
        List<SubOrder> subOrders;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class SubOrder {
        /**
         * 子订单开始的时间
         */
        Date startTime;
        /**
         * 子订单持续的时间 以小时为单位
         */
        Integer durationTimeInHour;
        /**
         * 处理该子订单的小组Id
         */
        String groupId;
        /**
         * 处理该子订单时所使用的机器Id
         */
        String machineId;
    }
}