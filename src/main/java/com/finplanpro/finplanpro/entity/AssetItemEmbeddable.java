package com.finplanpro.finplanpro.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetItemEmbeddable {
    private String name;
    private BigDecimal amount; // Present Value for current, Amount for future
    private BigDecimal expectedReturnRate;
}
