package com.finplanpro.finplanpro.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO สำหรับ Step 3: รายได้เสริมหลังเกษียณ (Concept ใหม่)
 */
@Data
public class Step3WantsDTO {
    // --- INPUT ---
    private String lifestyleChoice = "no_work"; // (no_work, light_work, heavy_work)
    private BigDecimal extraIncomePerMonth = BigDecimal.ZERO;

    // --- CALCULATED ---
    private int extraIncomeYears;
    private BigDecimal totalExtraIncome;
}
