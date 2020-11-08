package com.example.backend.service;

import com.example.backend.vo.ResourceAddVo;
import com.example.backend.vo.ResourceUpdateVo;
import com.example.backend.vo.ResponseVO;

import java.util.Map;

public interface ResourceService {

    ResponseVO getResourceOccupyByHour(String date);

    ResponseVO getResourceOccupyByDay(Map<String, String> date);

    ResponseVO getResourceLoadByDay(String startDate, String endDate);

    ResponseVO getResourceLoadByMonth(String startDate, String endDate);

    ResponseVO getResourceInfo();

    ResponseVO updateResourceInfo(ResourceUpdateVo resourceInfo);

    ResponseVO addResource(ResourceAddVo resourceInfo);

    ResponseVO deleteResource(String resourceId);
}
