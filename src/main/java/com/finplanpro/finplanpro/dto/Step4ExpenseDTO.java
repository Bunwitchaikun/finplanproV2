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
    private List<SpecialExpenseItem> items;

    // --- OUTPUT ---
    private BigDecimal totalSpecialExpensesFV; // มูลค่ารวมของค่าใช้จ่ายพิเศษทั้งหมด ณ วันเกษียณ

    @Data
    public static class SpecialExpenseItem {
        private String name;              // ชื่อรายการค่าใช้จ่าย
        private BigDecimal amountToday;     // จำนวนเงินในวันนี้
        private BigDecimal inflationRate;   // อัตราเงินเฟ้อสำหรับรายการนี้
        private int occurYear;            // ปีที่จะเกิดค่าใช้จ่าย
        private BigDecimal futureValue;     // (Output) มูลค่าในอนาคตของรายการนี้
    }
}
