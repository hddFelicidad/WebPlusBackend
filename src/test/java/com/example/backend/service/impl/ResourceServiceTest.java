package com.example.backend.service.impl;

import com.example.backend.dto.ScheduleOutputDto;
import com.example.backend.vo.ResponseVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.max;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ResourceServiceTest {

    @Autowired
    ResourceServiceImpl resourceService;

    @Autowired
    ScheduleServiceImpl scheduleService;

    @Test
    void getResourceLoadTest() throws ParseException {
        String s="2020-11-11 00:00:00";
        String e="2020-11-12 00:00:00";
        String flag="day";
        ResponseVO responseVO=ResponseVO.buildSuccess();
        ResourceServiceImpl mockRes=mock(ResourceServiceImpl.class);
        when(mockRes.getResourceLoad(s,e,flag)).thenReturn(responseVO);

        assert (mockRes.getResourceLoad(s,e,flag).getRet());
    }

    @Test
    void getResourceOccupyTest() throws ParseException {
        String s="2020-11-11 00:00:00";
        String e="2020-11-12 00:00:00";
        ResponseVO responseVO=ResponseVO.buildSuccess();
        ResourceServiceImpl mockRes=mock(ResourceServiceImpl.class);
        when(mockRes.getResourceOccupy(s,e)).thenReturn(responseVO);

        assert (mockRes.getResourceOccupy(s,e).getRet());
    }

    @Test
    void checkForDelayTest(){
        int size=scheduleService.tryGetScheduleOutput().getOrders().size();
        ScheduleOutputDto.Order order=scheduleService.tryGetScheduleOutput().getOrders().get(max(size-1,0));
        ResourceServiceImpl mockRes=mock(ResourceServiceImpl.class);
        when(mockRes.checkForDelay(order)).thenReturn("Success");

        assertEquals("Success",mockRes.checkForDelay(order));
    }


}
