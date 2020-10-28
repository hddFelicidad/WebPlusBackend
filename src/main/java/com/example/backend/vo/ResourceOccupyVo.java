package com.example.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceOccupyVo {
    /** 对象Id */
    Integer id;

    /** 资源名称 */
    String resource;

    /** 资源使用率 */
    String percent;

    /** 开始时间，格式为yyyy-MM-dd HH:mm */
    String start_date;

    /** 持续时间 */
    String duration;

    /** 产品名称 */
    String text;

    /** 产品颜色 */
    String color;

    /** 产品id */
    String product_id;

    /** 占用资源 */
    int parent;

}