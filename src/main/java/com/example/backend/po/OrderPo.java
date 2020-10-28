package com.example.backend.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class OrderPo {
    @Id
    String orderId;

    String itemId;

    Integer itemCount;

    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    Date deadLine;
}
