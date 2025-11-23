package com.finplanpro.finplanpro.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class InsuranceSummaryDto {

    private BigDecimal totalLifeCoverage;
    private BigDecimal totalDisabilityCoverage;
    private BigDecimal totalHealthCareRoom;
    private BigDecimal totalHealthCarePerVisit;
    private BigDecimal opdPerVisit;
    private BigDecimal compensationPerDay;
    private BigDecimal totalMainPremium;
    private BigDecimal totalRiderPremium;
    private BigDecimal accidentCoverage;
    private BigDecimal savingsReturn;
    private BigDecimal pension;
    private BigDecimal unitLinkedBenefits;
    private BigDecimal earlyMidCriticalIllness;
    private BigDecimal severeCriticalIllness;
    private BigDecimal partialAccidentCompensation;

    // Custom constructor for JPQL
    public InsuranceSummaryDto(BigDecimal totalLifeCoverage, BigDecimal totalDisabilityCoverage,
                               BigDecimal totalHealthCareRoom, BigDecimal totalHealthCarePerVisit,
                               BigDecimal opdPerVisit, BigDecimal compensationPerDay,
                               BigDecimal totalMainPremium, BigDecimal totalRiderPremium,
                               BigDecimal accidentCoverage, BigDecimal savingsReturn,
                               BigDecimal pension, BigDecimal unitLinkedBenefits,
                               BigDecimal earlyMidCriticalIllness, BigDecimal severeCriticalIllness,
                               BigDecimal partialAccidentCompensation) {
        this.totalLifeCoverage = totalLifeCoverage;
        this.totalDisabilityCoverage = totalDisabilityCoverage;
        this.totalHealthCareRoom = totalHealthCareRoom;
        this.totalHealthCarePerVisit = totalHealthCarePerVisit;
        this.opdPerVisit = opdPerVisit;
        this.compensationPerDay = compensationPerDay;
        this.totalMainPremium = totalMainPremium;
        this.totalRiderPremium = totalRiderPremium;
        this.accidentCoverage = accidentCoverage;
        this.savingsReturn = savingsReturn;
        this.pension = pension;
        this.unitLinkedBenefits = unitLinkedBenefits;
        this.earlyMidCriticalIllness = earlyMidCriticalIllness;
        this.severeCriticalIllness = severeCriticalIllness;
        this.partialAccidentCompensation = partialAccidentCompensation;
    }

    public BigDecimal getTotalPremium() {
        BigDecimal main = (totalMainPremium != null) ? totalMainPremium : BigDecimal.ZERO;
        BigDecimal rider = (totalRiderPremium != null) ? totalRiderPremium : BigDecimal.ZERO;
        return main.add(rider);
    }

    public BigDecimal getTotalCriticalIllnessCoverage() {
        BigDecimal early = (earlyMidCriticalIllness != null) ? earlyMidCriticalIllness : BigDecimal.ZERO;
        BigDecimal severe = (severeCriticalIllness != null) ? severeCriticalIllness : BigDecimal.ZERO;
        return early.add(severe);
    }
}
