package com.finplanpro.finplanpro.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Step4ExpenseDTO {
    private BigDecimal basicLivingCost;
    private BigDecimal healthCost;
    private BigDecimal familyCost;
    private BigDecimal specialEventsCost;
    private Double inflationRate;

    // computed
    private BigDecimal totalPostRetirementExpense;
}
