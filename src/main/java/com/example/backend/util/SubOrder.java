package com.example.backend.util;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Deprecated()
public class SubOrder {
    private String id;
    private String orderId;
    private Integer needHour;
    private List<String> availableGroupIdList;
    private List<String> availableMachineTypeIdList;
    private Integer deadLineTimeGrain;

    private Group group;

    private Machine machine;

    private Integer timeGrain;

    public SubOrder(String id, String orderId, Integer needHour, List<String> availableGroupIdList, List<String> availableMachineTypeIdList, Integer deadLineTimeGrain) {
        this.id = id;
        this.orderId = orderId;
        this.needHour = needHour;
        this.availableGroupIdList = availableGroupIdList;
        this.availableMachineTypeIdList = availableMachineTypeIdList;
        this.deadLineTimeGrain = deadLineTimeGrain;
    }
}