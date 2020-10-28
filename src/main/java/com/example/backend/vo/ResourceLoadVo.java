package com.example.backend.vo;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceLoadVo {
    /** 资源列表 */
    List<HashMap<String, String>> resourceList;

    /** 使用率数据 */
    List<HashMap<String, Object>> tableData;

    /** 生产线的总体资源使用率 */
    Integer resourceRate;

    /** 人力资源的总体资源使用率 */
    Integer humanRate;

}