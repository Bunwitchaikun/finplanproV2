package com.finplanpro.finplanpro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "net_worth_snapshots")
public class NetWorthSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String snapshotName;

    @Column(nullable = false)
    private LocalDate snapshotDate;

    // Assets
    private BigDecimal cashAndEquivalents = BigDecimal.ZERO;
    private BigDecimal stocks = BigDecimal.ZERO;
    private BigDecimal funds = BigDecimal.ZERO;
    private BigDecimal realEstate = BigDecimal.ZERO;
    private BigDecimal otherAssets = BigDecimal.ZERO;

    // Liabilities
    private BigDecimal creditCardDebt = BigDecimal.ZERO;
    private BigDecimal homeLoan = BigDecimal.ZERO;
    private BigDecimal carLoan = BigDecimal.ZERO;
    private BigDecimal otherLiabilities = BigDecimal.ZERO;

    // Calculated Fields
    private BigDecimal totalAssets = BigDecimal.ZERO;
    private BigDecimal totalLiabilities = BigDecimal.ZERO;
    private BigDecimal netWorth = BigDecimal.ZERO;

    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        totalAssets = cashAndEquivalents.add(stocks).add(funds).add(realEstate).add(otherAssets);
        totalLiabilities = creditCardDebt.add(homeLoan).add(carLoan).add(otherLiabilities);
        netWorth = totalAssets.subtract(totalLiabilities);
        if (snapshotDate == null) {
            snapshotDate = LocalDate.now();
        }
    }
}
