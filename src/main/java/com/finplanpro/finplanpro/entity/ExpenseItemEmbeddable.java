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
public class ExpenseItemEmbeddable {
    private String name;
    private BigDecimal amountToday;
    private BigDecimal inflationRate;
    private BigDecimal futureValue;

    // Type to distinguish between BASIC and SPECIAL expenses within the same
    // collection if needed,
    // or just rely on separate collections in the parent entity.
}
