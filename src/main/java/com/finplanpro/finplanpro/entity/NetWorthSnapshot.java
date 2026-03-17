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

    @OneToMany(mappedBy = "snapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<NetWorthItem> items = new java.util.ArrayList<>();

    // Calculated Fields
    private BigDecimal totalAssets = BigDecimal.ZERO;
    private BigDecimal totalLiabilities = BigDecimal.ZERO;
    private BigDecimal netWorth = BigDecimal.ZERO;

    public void addItem(NetWorthItem item) {
        items.add(item);
        item.setSnapshot(this);
    }

    public void removeItem(NetWorthItem item) {
        items.remove(item);
        item.setSnapshot(null);
    }

    @PrePersist
    @PreUpdate
    public void calculateTotals() {
        totalAssets = items.stream()
                .filter(i -> i.getType() == NetWorthItem.ItemType.ASSET)
                .map(NetWorthItem::getAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalLiabilities = items.stream()
                .filter(i -> i.getType() == NetWorthItem.ItemType.LIABILITY)
                .map(NetWorthItem::getAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        netWorth = totalAssets.subtract(totalLiabilities);

        if (snapshotDate == null) {
            snapshotDate = LocalDate.now();
        }
    }
}
