package com.finplanpro.finplanpro.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO สำหรับ Step 6: ผลลัพธ์การออกแบบและวิเคราะห์ช่องว่าง
 */
@Data
@Builder
public class DesignResultDTO {
    private BigDecimal monthlyCostAtRetirement; // ค่าใช้จ่ายต่อเดือน ณ วันเกษียณ
    private BigDecimal targetFund;              // เงินทุนที่ต้องมีสำหรับค่าใช้จ่ายดำรงชีพ
    private BigDecimal targetAll;               // เงินทุนเป้าหมายทั้งหมด (รวมค่าใช้จ่ายพิเศษ)
    private BigDecimal gap;                     // เงินทุนที่ยังขาด (ติดลบ) หรือเกิน (บวก)
    private BigDecimal requiredMonthlyInvestment; // เงินที่ต้องลงทุนเพิ่มต่อเดือน (PMT)
}
