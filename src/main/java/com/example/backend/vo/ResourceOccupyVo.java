package com.example.backend.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceOccupyVo {
    /** 资源Id */
    String id;

    /** 资源名称 */
    String name;

    /** 资源类型 */
    String type;

    /** 起始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    Date startTime;

    /** 终止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    Date endTime;

    /** 资源在起始时间到终止时间中正在处理的产品Id */
    String productId;

    /** 资源在起始时间到终止时间中正在处理的产品所属的订单Id */
    String orderId;

}