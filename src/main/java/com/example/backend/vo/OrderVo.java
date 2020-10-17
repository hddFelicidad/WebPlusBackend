package com.example.backend.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单基础信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo {
    /**
     * 订单Id
     */
    String id;

    /**
     * 需要生产的物品Id
     */
    String itemId;

    /**
     * 需要生产的物品数量
     */
    Integer itemCount;

    /**
     * 订单交付期限
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    Date deadLine;
}