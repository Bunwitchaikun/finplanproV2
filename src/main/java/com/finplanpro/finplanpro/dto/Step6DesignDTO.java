package com.finplanpro.finplanpro.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Step6DesignDTO {
    private Double expectedReturnRateBeforeRetirement;
    private Double expectedReturnRateAfterRetirement;

    // computed
    private BigDecimal totalNeeded;
    private BigDecimal gap;
    private BigDecimal monthlyInvestmentRequired;
}
