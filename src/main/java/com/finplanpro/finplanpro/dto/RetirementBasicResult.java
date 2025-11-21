package com.finplanpro.finplanpro.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RetirementBasicResult {
    private String planName;
    private int yearsToRetirement;
    private int yearsInRetirement;
    private BigDecimal retirementMonthlyExpense;
    private BigDecimal annualExpenseAtRetirement;
    private BigDecimal totalFundsNeeded;
    private BigDecimal requiredMonthlyInvestment;
}
