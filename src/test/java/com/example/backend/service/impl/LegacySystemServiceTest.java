package com.example.backend.service.impl;

import com.example.backend.service.impl.controllerWS.orderService.OrderEntity;
import com.example.backend.service.impl.controllerWS.personnelService.PersonnelEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LegacySystemServiceTest {

    @Autowired
    LegacySystemServiceImpl legacySystemService;

    @Test
    void getStaffInfoByIdTest(){
        PersonnelEntity staffInfo = legacySystemService.getStaffInfoById("1");
        assertEquals("1", staffInfo.getId());
        assertEquals("童玲", staffInfo.getName());
        assertEquals("组长", staffInfo.getPosition());
        assertEquals("5", staffInfo.getGroupId());
    }

    @Test
    void idAuthenticationTest(){
        String actualPosition = legacySystemService.idAuthentication("1");
        assertEquals("组长", actualPosition);
    }

    @Test
    void getAllOrders(){
        List<OrderEntity> orderEntityList = legacySystemService.getAllOrders();
        assertEquals(78,orderEntityList.size());
    }
}
