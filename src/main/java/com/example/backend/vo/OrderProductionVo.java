package com.example.backend.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 订单成产单关系 订单被拆分为子订单 每个子订单有一个生产单 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductionVo {
    /** 订单Id */
    String id;

    /** 拆分的子订单 如果不拆分则相当于只有一个子订单 */
    List<SubOrderProductionVo> subOrders;

    /** 子订单所对应的生产单 */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubOrderProductionVo {
        /** 子订单Id */
        String id;

        /** 生产单所需要用到的所有资源 */
        List<ProductionResourceVo> resources;

        /** 生产单中所有资源的所有生产任务 */
        List<ProductionTaskVo> tasks;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductionResourceVo {
        /** 资源Id */
        String id;

        /** 资源名称 */
        String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductionTaskVo {
        /** 资源Id */
        String resourceId;

        /** 资源名称 */
        String resourceName;

        /** 起始时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
        Date startTime;

        /** 终止时间 */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
        Date endTime;
    }
}