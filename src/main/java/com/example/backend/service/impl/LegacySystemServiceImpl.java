package com.example.backend.service.impl;

import com.example.backend.service.LegacySystemService;
import com.example.backend.service.impl.controllerWS.personnelService.PersonnelEntity;
import com.example.backend.service.impl.controllerWS.personnelService.PersonnelService;
import com.example.backend.service.impl.controllerWS.personnelService.PersonnelServiceSoap;
import org.springframework.stereotype.Service;

@Service
public class LegacySystemServiceImpl implements LegacySystemService {

    @Override
    public PersonnelEntity getStaffInfoById(String id){
        PersonnelService service = new PersonnelService();
        PersonnelServiceSoap serviceSoap = service.getPersonnelServiceSoapPort();
        return serviceSoap.getStaffInfoById(id);
    }

    @Override
    public String idAuthentication(String id){
        PersonnelService service = new PersonnelService();
        PersonnelServiceSoap serviceSoap = service.getPersonnelServiceSoapPort();
        return serviceSoap.idAuthentication(id);
    }
}
