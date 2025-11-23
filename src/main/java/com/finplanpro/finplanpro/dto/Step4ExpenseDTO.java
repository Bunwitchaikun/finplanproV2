package com.finplanpro.finplanpro.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO สำหรับ Step 4: ค่าใช้จ่ายพิเศษ (Reverted to original structure)
 */
@Data
public class Step4ExpenseDTO {
    // --- INPUT ---
    private List<ExpenseItem> basicItems = new ArrayList<>();
    private List<ExpenseItem> specialItems = new ArrayList<>();

    // --- OUTPUT ---
    private BigDecimal totalBasicExpensesToday;
    private BigDecimal totalBasicExpensesFV;
    private BigDecimal totalSpecialExpensesToday;
    private BigDecimal totalSpecialExpensesFV;
    private BigDecimal totalRetirementExpensesToday;
    private BigDecimal totalRetirementExpensesFV;

    @Data
    public static class ExpenseItem {
        private String name;
        private BigDecimal amountToday = BigDecimal.ZERO;
        private BigDecimal inflationRate = new BigDecimal("3.0");
        private BigDecimal futureValue;
    }
}
