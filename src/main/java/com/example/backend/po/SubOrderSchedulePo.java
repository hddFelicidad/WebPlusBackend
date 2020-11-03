package com.example.backend.po;

import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubOrderSchedulePo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String suborderId;

    private Date startTime;

    private Integer durationTimeInHour;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> groupIdList;

    private String machineId;
}