package com.example.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import com.example.backend.vo.ResourceLoadVo;
import com.example.backend.vo.ResourceOccupyVo;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "resource")
public class ResourceController {
    /**
     * 查询从起始日期到终止日期每个资源的负载情况
     * 
     * @param startDate 起始日期
     * @param endDate   终止日期
     */
    @GetMapping(value = "/load")
    public List<ResourceLoadVo> getLoad(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        // TODO:
        return null;
    }

    /**
     * 获取指定日期内每个资源的占用情况
     */
    @GetMapping(value = "/occupy")
    public List<ResourceOccupyVo> getOccupy(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        // TODO:
        return null;
    }

}