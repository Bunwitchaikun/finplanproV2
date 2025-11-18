package com.finplanpro.finplanpro.entity;

import jakarta.persistence.*;
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
    private int currentAge;
    private int retireAge;
    private BigDecimal monthlyExpense;
    private double inflationRate;
    private int lifeExpectancy;
    private double preRetireReturn;
    private double postRetireReturn;

    // Calculation result
    private BigDecimal totalFundsNeeded;
    
    private String planName;
}
