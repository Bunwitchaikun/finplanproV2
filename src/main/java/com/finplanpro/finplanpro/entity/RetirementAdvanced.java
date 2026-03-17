package com.finplanpro.finplanpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "retirement_advanced")
public class RetirementAdvanced {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String planName;

    // Step 1: YOU
    private LocalDate dateOfBirth;
    private String gender;
    private Integer retireAge;

    // Step 2: LIFE
    private Integer lifeExpectancy;

    // Step 3: WANT
    private String lifestyle; // e.g., BASIC, COMFORTABLE, LUXURY

    // Step 4: EXPENSE
    private BigDecimal desiredMonthlyExpense; // Desired cost in today's value
    private BigDecimal specialExpense; // One-time special cost

    @ElementCollection
    @CollectionTable(name = "retirement_basic_expenses", joinColumns = @JoinColumn(name = "retirement_id"))
    private java.util.List<ExpenseItemEmbeddable> basicExpenses = new java.util.ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "retirement_special_expenses", joinColumns = @JoinColumn(name = "retirement_id"))
    private java.util.List<ExpenseItemEmbeddable> specialExpenses = new java.util.ArrayList<>();

    // Step 5: HAVES
    private BigDecimal currentAssets; // Liquid assets for retirement
    private BigDecimal rmfSsf;
    private BigDecimal pension;
    private BigDecimal annuity;

    @ElementCollection
    @CollectionTable(name = "retirement_current_assets", joinColumns = @JoinColumn(name = "retirement_id"))
    private java.util.List<AssetItemEmbeddable> currentAssetItems = new java.util.ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "retirement_future_assets", joinColumns = @JoinColumn(name = "retirement_id"))
    private java.util.List<AssetItemEmbeddable> futureAssetItems = new java.util.ArrayList<>();

    // Step 6 & 7: DESIGN & TEST (Results)
    private BigDecimal totalFundsNeeded;
    private BigDecimal fundGap; // Positive if surplus, negative if shortfall
}
