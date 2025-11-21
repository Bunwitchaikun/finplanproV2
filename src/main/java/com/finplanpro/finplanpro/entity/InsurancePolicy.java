package com.finplanpro.finplanpro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "insurance_policies")
public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String policyNumber;

    // Coverage Details
    private BigDecimal lifeCoverage = BigDecimal.ZERO;
    private BigDecimal disabilityCoverage = BigDecimal.ZERO;
    private BigDecimal healthCareRoom = BigDecimal.ZERO;
    private BigDecimal healthCarePerVisit = BigDecimal.ZERO;
    private BigDecimal healthCareOpd = BigDecimal.ZERO;
    private BigDecimal dailyCompensation = BigDecimal.ZERO;
    private BigDecimal criticalIllnessCoverage = BigDecimal.ZERO;

    // Premiums
    private BigDecimal mainPremium = BigDecimal.ZERO;
    private BigDecimal riderPremium = BigDecimal.ZERO;

    @Transient
    public BigDecimal getTotalPremium() {
        return mainPremium.add(riderPremium);
    }
}
