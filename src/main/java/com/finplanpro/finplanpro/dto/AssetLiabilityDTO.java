package com.finplanpro.finplanpro.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for D5 Assets & Liabilities Management step.
 */
@Data
public class AssetLiabilityDTO {
    // --- INPUT ---
    private List<Item> assetItems; // Pre-defined + custom assets
    private List<Item> liabilityItems; // Pre-defined + custom liabilities

    // --- OUTPUT ---
    private BigDecimal totalAssets; // Sum of asset amounts (today value)
    private BigDecimal totalLiabilities; // Sum of liability amounts (today value)
    private BigDecimal netWorth; // totalAssets - totalLiabilities

    @Data
    public static class Item {
        private String name; // Item name (e.g., "เงินสด", "บัตรเครดิต")
        private BigDecimal amount; // Amount entered by user (today value)
    }
}
