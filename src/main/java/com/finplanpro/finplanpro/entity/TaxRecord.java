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
@Table(name = "tax_records")
public class TaxRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int taxYear;

    // Income
    private BigDecimal monthlyIncome = BigDecimal.ZERO;
    private BigDecimal otherAnnualIncome = BigDecimal.ZERO;

    // Deductions
    private boolean hasSpouse = false;
    private int childrenCount = 0;
    private int parentCount = 0;
    private int disabledCareCount = 0;

    // Insurance & Funds
    private BigDecimal lifeInsurancePremium = BigDecimal.ZERO;
    private BigDecimal healthInsurancePremium = BigDecimal.ZERO;
    private BigDecimal parentHealthInsurancePremium = BigDecimal.ZERO;
    private BigDecimal pensionInsurancePremium = BigDecimal.ZERO;
    private BigDecimal providentFund = BigDecimal.ZERO;
    private BigDecimal rmf = BigDecimal.ZERO;
    private BigDecimal ssf = BigDecimal.ZERO;
    private BigDecimal nac = BigDecimal.ZERO; // National Savings Fund (กอช.)

    // Calculated Results
    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal totalDeduction = BigDecimal.ZERO;
    private BigDecimal netTaxableIncome = BigDecimal.ZERO;
    private BigDecimal taxPayable = BigDecimal.ZERO;
}
