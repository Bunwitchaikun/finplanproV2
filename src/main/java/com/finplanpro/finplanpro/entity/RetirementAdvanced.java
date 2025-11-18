package com.finplanpro.finplanpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "retirement_advanced")
public class RetirementAdvanced {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    private String planName;

    // Step 1: YOU
    private LocalDate dateOfBirth;
    private String gender;
    private int retireAge;

    // Step 2: LIFE
    private int lifeExpectancy;

    // Step 3: WANT
    private String lifestyle; // e.g., BASIC, COMFORTABLE, LUXURY

    // Step 4: EXPENSE
    private BigDecimal desiredMonthlyExpense; // Desired cost in today's value
    private BigDecimal specialExpense; // One-time special cost

    // Step 5: HAVES
    private BigDecimal currentAssets; // Liquid assets for retirement
    private BigDecimal rmfSsf;
    private BigDecimal pension;
    private BigDecimal annuity;

    // Step 6 & 7: DESIGN & TEST (Results)
    private BigDecimal totalFundsNeeded;
    private BigDecimal fundGap; // Positive if surplus, negative if shortfall
}
