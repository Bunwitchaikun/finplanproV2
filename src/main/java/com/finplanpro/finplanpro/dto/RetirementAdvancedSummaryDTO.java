package com.finplanpro.finplanpro.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class RetirementAdvancedSummaryDTO {
    private UUID planId;
    private Integer currentStep;
    private BigDecimal targetFund;
    private BigDecimal monthlyInvestmentRequired;
    private Integer finalLifeExpectancy;
    private Map<String, Object> steps;
}
