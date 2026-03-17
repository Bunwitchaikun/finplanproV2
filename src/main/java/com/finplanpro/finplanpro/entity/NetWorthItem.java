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
@Table(name = "net_worth_items")
public class NetWorthItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snapshot_id", nullable = false)
    private NetWorthSnapshot snapshot;

    private String name;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    public enum ItemType {
        ASSET, LIABILITY
    }

    public NetWorthItem(String name, ItemType type) {
        this.name = name;
        this.type = type;
        // amount remains null by default
    }
}
