package com.example.backend.service;

import com.example.backend.service.impl.controllerWS.personnelService.PersonnelEntity;

public interface LegacySystemService {

    //获取员工信息
    PersonnelEntity getStaffInfoById(String id);

    //进行身份认证
    String idAuthentication(String id);
}
