package com.example.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceAddVo {

    String date;

    Integer type;

    String name;

    Integer number;

    Integer shift;
}
