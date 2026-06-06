package com.amusementpark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {

    private Long ticketTypeId;
    private String ticketTypeName;
    private double unitPrice;
    private int  quantity;
    private boolean vip;
    private int zoneCount;
    private LocalDate startDate;
    private LocalDate endDate;

}
