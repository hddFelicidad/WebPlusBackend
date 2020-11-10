package com.example.backend.controller;

import com.example.backend.service.ResourceService;
import com.example.backend.vo.ResourceAddVo;
import com.example.backend.vo.ResourceUpdateVo;
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
     * 查询从起始日期到终止日期每个资源的负载情况（按天显示）
     * 
     * @param start_date 开始日期
     * @param end_date   结束日期
     * @return
     */
    @GetMapping(value = "/percent/{start_date}/{end_date}")
    public ResponseVO getResourceLoadByDay(@PathVariable("start_date") String start_date,
            @PathVariable("end_date") String end_date) {
        return resourceService.getResourceLoadByDay(start_date, end_date);
    }

    /**
     * 查询从起始日期到终止日期每个资源的负载情况（按月显示）
     * 
     * @param start_date 开始日期
     * @param end_date   结束日期
     * @return
     */
    @GetMapping(value = "/percent/month/{start_date}/{end_date}")
    public ResponseVO getResourceLoadByMonth(@PathVariable("start_date") String start_date,
            @PathVariable("end_date") String end_date) {
        return resourceService.getResourceLoadByMonth(start_date, end_date);
    }

    /**
     * 获取资源甘特图（按小时显示）
     * 
     * @param date 日期
     * @return
     */
    @GetMapping(value = "/resource-{date_str}")
    public ResponseVO getResourceOccupyByHour(@PathVariable("date_str") String date) {
        return resourceService.getResourceOccupyByHour(date);
    }

    /**
     * 获取资源甘特图（按天数显示）
     * 
     * @param date 起止时间
     * @return
     */
    @PostMapping(value = "/resource")
    public ResponseVO getResourceLoadByDay(@RequestBody Map<String, String> date) {
        return resourceService.getResourceOccupyByDay(date);
    }

    @GetMapping(value = "/resource/occupyInfo/{date}")
    public ResponseVO getResourceOccupyInfo(@PathVariable("date") String date) {
        return resourceService.getResourceOccupyInfo(date);
    }

    // 以下是资源的增删改查
    @GetMapping(value = "/resourceInfo")
    public ResponseVO getResourceInfo() {
        return resourceService.getResourceInfo();
    }

    @PostMapping(value = "/updateResource")
    public ResponseVO updateResourceInfo(@RequestBody ResourceUpdateVo resourceInfo) {
        return resourceService.updateResourceInfo(resourceInfo);
    }

    @PostMapping(value = "/addResource")
    public ResponseVO addResource(@RequestBody ResourceAddVo resourceInfo) {
        return resourceService.addResource(resourceInfo);
    }

    @GetMapping(value = "/deleteResource/{id}")
    public ResponseVO deleteResource(@PathVariable("id") String resourceId) {
        return resourceService.deleteResource(resourceId);
    }
}