package com.adedayo.AdexBank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrDrRequest {
    private String accountNumber;
    private BigDecimal amount;
}
