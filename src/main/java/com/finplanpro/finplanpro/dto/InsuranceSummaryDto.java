package com.finplanpro.finplanpro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceSummaryDto {
    private BigDecimal totalLifeCoverage;
    private BigDecimal totalDisabilityCoverage;
    private BigDecimal totalHealthCareRoom;
    private BigDecimal totalHealthCarePerVisit;
    private BigDecimal totalHealthCareOpd;
    private BigDecimal totalDailyCompensation;
    private BigDecimal totalCriticalIllnessCoverage;
    private BigDecimal totalMainPremium;
    private BigDecimal totalRiderPremium;

    public BigDecimal getTotalPremium() {
        return totalMainPremium.add(totalRiderPremium);
    }
}
