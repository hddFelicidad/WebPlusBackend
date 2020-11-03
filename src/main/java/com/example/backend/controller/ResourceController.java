package com.example.backend.controller;

import com.example.backend.service.ResourceService;
import com.example.backend.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class ResourceController {
    @Autowired
    ResourceService resourceService;

    /**
     * 查询从起始日期到终止日期每个资源的负载情况
     * @param date 包含起始日期和结束日期的数组
     * @return
     */
    @PostMapping(value = "/percent")
    public ResponseVO getLoad(@RequestBody Map<String, String> date) {
        return resourceService.getResourceLoadByDay(date);
    }

    /**
     * 获取资源甘特图（按小时显示）
     * @param date 日期
     * @return
     */
    @GetMapping(value = "/resource-{date_str}")
    public ResponseVO getResourceOccupyByHour(@PathVariable("date_str") String date) {
        return resourceService.getResourceOccupyByHour(date);
    }

    /**
     * 获取资源甘特图（按天数显示）
     * @param date 起止时间
     * @return
     */
    @PostMapping(value = "/resource")
    public ResponseVO getResourceLoadByDay(@RequestBody Map<String, String> date){
        return resourceService.getResourceOccupyByDay(date);
    }
}