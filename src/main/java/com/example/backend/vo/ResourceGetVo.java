package com.example.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceGetVo {

    String id;

    String date;

    String resourceName;

    Integer resourceCount;

    Integer shift;
}
