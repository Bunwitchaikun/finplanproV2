package com.finplanpro.finplanpro.dto;

import lombok.Data;

/**
 * DTO สำหรับ Step 1: ข้อมูลเกี่ยวกับตัวคุณ
 */
@Data
public class Step1YouDTO {
    // --- INPUT ---
    private Integer currentAge; // อายุปัจจุบัน
    private String gender; // เพศ
    private Integer retirementAge; // อายุที่ต้องการเกษียณ

    // --- OUTPUT ---
    private Integer yearsToRetirement; // จำนวนปีที่เหลือก่อนเกษียณ (calculated)
}
