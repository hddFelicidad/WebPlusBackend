package com.example.backend.controller;

import com.example.backend.vo.ResponseVO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class ResourceController {
    /**
     * 查询从起始日期到终止日期每个资源的负载情况
     * @param date 包含起始日期和结束日期的数组
     * @return
     */
    @PostMapping(value = "/percent")
    public ResponseVO getLoad(@RequestBody Map<String, String> date) {
        // TODO:
        return null;
    }

    /**
     * 获取指定日期内每个资源的占用情况
     */
    @GetMapping(value = "/resource-{date_str}")
    public ResponseVO getOccupy(@PathVariable("date_str") String date) {
        // TODO:
        return null;
    }

}