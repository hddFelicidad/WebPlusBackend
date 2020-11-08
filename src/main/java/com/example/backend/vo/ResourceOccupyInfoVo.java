package com.example.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceOccupyInfoVo {

    String resourceId;

    String resourceName;

    List<OccupyInfo> occupyInfoList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OccupyInfo{
        String startTime;

        String endTime;

        String orderId;

        String subOrderId;
    }
}
