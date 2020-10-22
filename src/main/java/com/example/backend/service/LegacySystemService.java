package com.example.backend.service;

import com.example.backend.service.impl.controllerWS.attendanceService.*;
import com.example.backend.service.impl.controllerWS.orderService.*;
import com.example.backend.service.impl.controllerWS.personnelService.*;
import com.example.backend.service.impl.controllerWS.erpService.*;

import java.util.List;

public interface LegacySystemService {

    //获取员工信息
    PersonnelEntity getStaffInfoById(String id);

    //进行身份认证
    String idAuthentication(String id);

    //获取订单信息
    OrderEntity getOrderInfoById(String id);

    //获取班次信息
    List<ClassEntity> getClassInfo();

    //获取人力资源（班组）排班信息
    List<CalendarEntity> getCalendarInfo();

    //获取人力资源（班组）信息
    List<ResourceEntity> getResourceTeamInfo();

    //获取物品信息
    MaterialEntity getMaterialInfoById(String id);

    //获取生产线/设备资源信息
    LineEntity getLineResourceById(String id);

    //获取生产线/设备资源信息
    List<LineEntity> getAllLineResources();

    //获取BOM信息
    BomEntity getBOMById(String id);

    //获取BOM信息
    List<BomEntity> getAllBOMs();
}
