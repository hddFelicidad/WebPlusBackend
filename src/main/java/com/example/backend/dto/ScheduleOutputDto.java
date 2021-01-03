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
    public static class Order {
        /**
         * 订单id
         */
        private String id;
        /**
         * 完成本订单之前需要完成的其他订单的id，比如测试订单需要在装配订单之后
         */
        private String requiredOrderId;
        private List<SubOrder> subOrders;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubOrder {
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

        public Date getEndTime() {
            return new Date(startTime.getTime() + durationTimeInHour * 60L * 60L * 1000L);
        }
    }
}