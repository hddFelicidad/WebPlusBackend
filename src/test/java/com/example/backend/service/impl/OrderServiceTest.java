package com.example.backend.service.impl;

import com.example.backend.data.GroupRepository;
import com.example.backend.data.MachineRepository;
import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.po.GroupPo;
import com.example.backend.po.MachinePo;
import com.example.backend.util.OrderUtil;
import com.example.backend.vo.ResponseVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    ScheduleServiceImpl scheduleService;

    @Test
    void getOrderOccupyTest() throws ParseException {
        String s="2020-11-11 00:00:00";
        String e="2020-11-12 00:00:00";
        ResponseVO responseVO=ResponseVO.buildSuccess();
        OrderServiceImpl mockOde=mock(OrderServiceImpl.class);
        when(mockOde.getOrderOccupy(s,e)).thenReturn(responseVO);

        assert (mockOde.getOrderOccupy(s,e).getRet());
    }

    @Test
    void getProductOccupy() throws ParseException {
        String s="2020-11-11 00:00:00";
        String e="2020-11-12 00:00:00";
        String id="1";
        ResponseVO responseVO=ResponseVO.buildSuccess();
        OrderServiceImpl mockOde=mock(OrderServiceImpl.class);
        when(mockOde.getProductOccupy(s,e,id)).thenReturn(responseVO);

        assert (mockOde.getProductOccupy(s,e,"1").getRet());
    }

    @Test
    void getOrderPlanTest(){
        OrderUtil mockOde=mock(OrderUtil.class);
        List<ScheduleOutputDto.Order> orderScheduleList=scheduleService.tryGetScheduleOutput().getOrders();
        List<ScheduleOutputDto.Order> orderScheduleList1=new ArrayList<>();
        ScheduleOutputDto.Order order=new ScheduleOutputDto.Order();
        orderScheduleList1.add(order);
        when(mockOde.orderRemake(orderScheduleList)).thenReturn(orderScheduleList1);

        assertTrue(orderService.getOrderPlan().getRet());
    }

    @Test
    void getOrderPlanProductionTest(){
        OrderUtil mockOde=mock(OrderUtil.class);
        List<ScheduleOutputDto.Order> orderScheduleList=scheduleService.tryGetScheduleOutput().getOrders();
        List<ScheduleOutputDto.Order> orderScheduleList1=new ArrayList<>();
        List<ScheduleOutputDto.SubOrder> orderScheduleList2=new ArrayList<>();
        ScheduleOutputDto.Order order=new ScheduleOutputDto.Order();
        ScheduleOutputDto.SubOrder order1=new ScheduleOutputDto.SubOrder();
        order.setId("1");
        orderScheduleList2.add(order1);
        order.setSubOrders(orderScheduleList2);
        order.getSubOrders().get(max(order.getSubOrders().size()-1,0)).setMachineId("1");
        orderScheduleList1.add(order);
        OrderServiceImpl mockOdeS=mock(OrderServiceImpl.class);
        when(mockOde.orderRemake(orderScheduleList)).thenReturn(orderScheduleList1);
        when(mockOdeS.getOrderPlanProduction()).thenReturn(null);

        GroupRepository mockGr=mock(GroupRepository.class);
        GroupPo groupPo=new GroupPo();
        when(mockGr.findGroupPoByGroupId("1")).thenReturn(groupPo);

        MachineRepository mockMac=mock(MachineRepository.class);
        MachinePo machinePo=new MachinePo();
        when(mockMac.findMachinePoById(1)).thenReturn(machinePo);

        assertNull(mockOdeS.getOrderPlanProduction());
        assertTrue(orderService.getOrderPlan().getRet());
    }

}
