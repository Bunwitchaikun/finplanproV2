package com.finplanpro.finplanpro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "retirement_basic")
public class RetirementBasic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Input fields
    @NotNull
    @Min(0)
    private Integer currentAge;

    @NotNull
    @Min(1)
    private Integer retireAge;

    @NotBlank
    private String planName;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal monthlyExpense;

    @DecimalMin(value = "0.0")
    private double inflationRate;

    @NotNull
    @Min(1)
    private Integer lifeExpectancy;

    @DecimalMin(value = "0.0")
    private double preRetireReturn;

    @DecimalMin(value = "0.0")
    private double postRetireReturn;

    // Calculation result
    private BigDecimal totalFundsNeeded;
    
    // Derived values (not persisted)
    @Transient
    private BigDecimal retirementMonthlyExpense;

    @Transient
    private BigDecimal annualExpenseAtRetirement;

    @Transient
    private BigDecimal requiredMonthlyInvestment;

    @Transient
    private Integer yearsToRetirement;
}
