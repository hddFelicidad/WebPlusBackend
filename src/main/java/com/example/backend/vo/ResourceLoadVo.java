package com.example.backend.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceLoadVo {
    /** 资源Id */
    String id;

    /** 资源名称 */
    String name;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    Date date;

    /** 负载率 取值在[0, 1]之间 */
    Double loadRate;

}