package com.finplanpro.finplanpro.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Step3WantsDTO {
    private BigDecimal currentMonthlyExpense;
    private Double inflationRate = 0.03;
    private Boolean wantsAdditionalIncome;
    private BigDecimal additionalIncomeAmount;
    private Integer additionalIncomeYears;

    // computed
    private BigDecimal futureExpense;
}
