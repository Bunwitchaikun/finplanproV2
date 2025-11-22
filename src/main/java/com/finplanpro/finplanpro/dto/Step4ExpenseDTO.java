package com.finplanpro.finplanpro.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO สำหรับ Step 4: ค่าใช้จ่ายพิเศษ
 */
@Data
public class Step4ExpenseDTO {
    // --- INPUT ---
    private List<ExpenseItem> basicItems;
    private List<ExpenseItem> specialItems;

    // --- OUTPUT ---
    private BigDecimal totalBasicExpensesToday;
    private BigDecimal totalBasicExpensesFV;

    private BigDecimal totalSpecialExpensesToday;
    private BigDecimal totalSpecialExpensesFV;

    private BigDecimal totalRetirementExpensesToday;
    private BigDecimal totalRetirementExpensesFV;

    @Data
    public static class ExpenseItem {
        private String name; // ชื่อรายการค่าใช้จ่าย
        private BigDecimal amountToday; // จำนวนเงินในวันนี้
        private BigDecimal inflationRate; // อัตราเงินเฟ้อสำหรับรายการนี้
        private BigDecimal futureValue; // (Output) มูลค่าในอนาคตของรายการนี้
    }
}
