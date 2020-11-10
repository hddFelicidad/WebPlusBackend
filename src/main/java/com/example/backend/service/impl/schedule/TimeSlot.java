package com.example.backend.service.impl.schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd HH");

    int index;
    LocalDateTime time;

    @Override
    public String toString() {
        return "{ " + index + ", " + time.format(FORMATTER) + " }";
    }
}