package com.example.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrgentOrderVo {

    String productId;

    String quantity;

    String date;
}
