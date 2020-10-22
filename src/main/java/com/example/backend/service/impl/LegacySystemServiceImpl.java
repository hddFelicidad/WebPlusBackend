package com.example.backend.service.impl;

import com.example.backend.service.LegacySystemService;
import com.example.backend.service.impl.controllerWS.attendanceService.*;
import com.example.backend.service.impl.controllerWS.erpService.*;
import com.example.backend.service.impl.controllerWS.orderService.*;
import com.example.backend.service.impl.controllerWS.personnelService.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegacySystemServiceImpl implements LegacySystemService {

    @Override
    public PersonnelEntity getStaffInfoById(String id){
        PersonnelService personnelService = new PersonnelService();
        PersonnelServiceSoap serviceSoap = personnelService.getPersonnelServiceSoapPort();
        return serviceSoap.getStaffInfoById(id);
    }

    @Override
    public String idAuthentication(String id){
        PersonnelService personnelService = new PersonnelService();
        PersonnelServiceSoap serviceSoap = personnelService.getPersonnelServiceSoapPort();
        return serviceSoap.idAuthentication(id);
    }

    @Override
    public OrderEntity getOrderInfoById(String id){
        OrderService orderService = new OrderService();
        OrderServiceSoap serviceSoap = orderService.getOrderServiceSoapPort();
        return serviceSoap.getOrderInfoById(id);
    }

    @Override
    public List<ClassEntity> getClassInfo() {
        AttendanceService attendanceService = new AttendanceService();
        AttendanceServiceSoap serviceSoap = attendanceService.getAttendanceServiceSoapPort();
        return serviceSoap.getClassInfo();
    }

    @Override
    public List<CalendarEntity> getCalendarInfo() {
        AttendanceService attendanceService = new AttendanceService();
        AttendanceServiceSoap serviceSoap = attendanceService.getAttendanceServiceSoapPort();
        return serviceSoap.getCalendarInfo();
    }

    @Override
    public List<ResourceEntity> getResourceTeamInfo() {
        ERPService erpService = new ERPService();
        ERPServiceSoap serviceSoap = erpService.getERPServiceSoapPort();
        return serviceSoap.getResourceTeamInfo();
    }

    @Override
    public MaterialEntity getMaterialInfoById(String id) {
        ERPService erpService = new ERPService();
        ERPServiceSoap serviceSoap = erpService.getERPServiceSoapPort();
        return serviceSoap.getMaterialInfoById(id);
    }

    @Override
    public LineEntity getLineResourceById(String id) {
        ERPService erpService = new ERPService();
        ERPServiceSoap serviceSoap = erpService.getERPServiceSoapPort();
        return serviceSoap.getLineResourceById(id);
    }

    @Override
    public List<LineEntity> getAllLineResources() {
        ERPService erpService = new ERPService();
        ERPServiceSoap serviceSoap = erpService.getERPServiceSoapPort();
        return serviceSoap.getAllLineResources();
    }

    @Override
    public BomEntity getBOMById(String id) {
        ERPService erpService = new ERPService();
        ERPServiceSoap serviceSoap = erpService.getERPServiceSoapPort();
        return serviceSoap.getBOMById(id);
    }

    @Override
    public List<BomEntity> getAllBOMs() {
        ERPService erpService = new ERPService();
        ERPServiceSoap serviceSoap = erpService.getERPServiceSoapPort();
        return serviceSoap.getAllBOMs();
    }

}
