package com.example.backend.service.impl;

import com.example.backend.service.impl.controllerWS.attendanceService.CalendarEntity;
import com.example.backend.service.impl.controllerWS.attendanceService.ClassEntity;
import com.example.backend.service.impl.controllerWS.erpService.BomEntity;
import com.example.backend.service.impl.controllerWS.erpService.LineEntity;
import com.example.backend.service.impl.controllerWS.erpService.ResourceEntity;
import com.example.backend.service.impl.controllerWS.orderService.OrderEntity;
import com.example.backend.service.impl.controllerWS.personnelService.PersonnelEntity;
import com.example.backend.service.impl.controllerWS.personnelService.PersonnelServiceSoap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LegacySystemServiceTest {

    @Autowired
    LegacySystemServiceImpl legacySystemService;

    @Test
    void getStaffInfoByIdTest() throws NoSuchFieldException {
        PersonnelEntity staffInfo =new PersonnelEntity();
        staffInfo.setGroupId("1");
        staffInfo.setId("1");

        PersonnelServiceSoap mockPS=mock(PersonnelServiceSoap.class);
        when(mockPS.getStaffInfoById("1")).thenReturn(staffInfo);

//        Field declaredField = LegacySystemServiceImpl.class.getDeclaredField("personnelSoap");
//        declaredField.setAccessible(true);
//        ReflectionUtils.setField(declaredField, legacySystemService, mockPS);

        assertEquals("1",legacySystemService.getStaffInfoById("1").getId());

    }

    @Test
    void idAuthenticationTest(){
        String actualPosition = legacySystemService.idAuthentication("1");
        assertEquals("组长", actualPosition);
    }

    @Test
    void getClassInfoTest(){
        List<ClassEntity> orderEntity=legacySystemService.getClassInfo();
        assertEquals(4,orderEntity.size());
    }

    @Test
    void getCalendarInfoTest(){
        List<CalendarEntity> calendarEntities=legacySystemService.getCalendarInfo();
        assertEquals(65,calendarEntities.size());
    }

    @Test
    void getResourceTeamInfoTest(){
        List<ResourceEntity> resourceEntities=legacySystemService.getResourceTeamInfo();
        assertEquals(65,resourceEntities.size());
    }

    @Test
    void getAllLineResourcesTest(){
        List<LineEntity> lineEntities=legacySystemService.getAllLineResources();
        assertEquals(31,lineEntities.size());
    }

    @Test
    void getAllBOMsTest(){
        List<BomEntity> bomEntities=legacySystemService.getAllBOMs();
        assertEquals(8,bomEntities.size());
    }

    @Test
    void getAllOrders(){
        List<OrderEntity> orderEntityList = legacySystemService.getAllOrders();
        assertEquals(78,orderEntityList.size());
    }
}
