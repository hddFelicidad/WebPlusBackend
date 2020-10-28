package com.example.backend.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.example.backend.vo.ResourceLoadVo;
import com.example.backend.vo.ResourceOccupyVo;

@RestController
@RequestMapping
public class ResourceController {
    /**
     * 查询从起始日期到终止日期每个资源的负载情况
     * @param date 包含起始日期和结束日期的数组
     * @return
     */
    @PostMapping(value = "/percent")
    public List<ResourceLoadVo> getLoad(@RequestBody Map<String, String> date) {
        // TODO:
        return null;
    }

    /**
     * 获取指定日期内每个资源的占用情况
     */
    @GetMapping(value = "/resource-{date_str}")
    public List<ResourceOccupyVo> getOccupy(@PathVariable("date_str") String date) {
        // TODO:
        return null;
    }

}