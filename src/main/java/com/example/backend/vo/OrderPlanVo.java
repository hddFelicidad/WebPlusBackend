package com.example.backend.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlanVo {
    /** 订单Id */
    String id;

    /** 所有的子订单 如果不拆分则相当于只有一个子订单 */
    List<SubOrderPlanVo> subOrders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubOrderPlanVo {
        /** 子订单Id */
        String id;

        /** 子订单预计完成时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
        Date startTime;

        /** 子订单预计完成时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
        Date finishTime;
    }

}