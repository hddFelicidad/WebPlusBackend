package com.example.backend.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderOccupyVo {
    /** 对象Id */
    Integer id;

    /** 订单号 */
    String number;

    /** 任务名称 */
    String text;

    /** 完成度 */
    float progress;

    /** 产品名称 */
    String name;

    /** 约定交期 */
    String deal_date;

    /** 预计交期 */
    String expc_date;

    /** 父任务id */
    Integer parent;

}
