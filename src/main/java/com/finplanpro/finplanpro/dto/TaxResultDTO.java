package com.finplanpro.finplanpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxResultDTO {
    private BigDecimal annualIncome;
    private BigDecimal totalExpenseDeduction;
    private BigDecimal totalAllowance;
    private BigDecimal netIncome;
    private BigDecimal taxAmount;
}
