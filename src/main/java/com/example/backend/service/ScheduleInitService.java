package com.example.backend.service;

import com.example.backend.dto.ScheduleInputDto;

import java.util.Date;

public interface ScheduleInitService {

    void scheduleInit(Date startDate);

    ScheduleInputDto getScheduleInput();
}
