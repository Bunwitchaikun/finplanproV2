package com.finplanpro.finplanpro.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Step7SimulationDTO {
    private String scenario;
    private Integer ageMoneyRunsOut;
    private BigDecimal requiredMonthlyInvestment;
    private Boolean survivalStatus;
}
