package com.example.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 订单的状态 因为只考虑装配环节，所以装配环节的进度就是整个订单的进度 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusVo {
    /** 订单Id */
    String id;

    /** 订单要求的总产品数量 */
    Integer itemCount;

    /** 整个订单的进度，当前已完成产品与总产品数量的比值 取值在[0, 1]之间 */
    Double progress;

    /** 延期比例，在交付期限之后完成的产品与总产品数量的比值 取值在[0, 1]之间 */
    Double lateRate;

}