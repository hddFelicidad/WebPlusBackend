package com.example.backend.service;

import com.example.backend.dto.ScheduleInputDto;
import com.example.backend.vo.ResponseVO;

import java.util.Date;

public interface ScheduleInitService {

    ResponseVO scheduleInit(Date startDate);

    ScheduleInputDto getScheduleInput();
}
