package com.finplanpro.finplanpro.dto;

import lombok.Data;

/**
 * DTO สำหรับ Step 1: ข้อมูลเกี่ยวกับตัวคุณ (Aligned with Form)
 */
@Data
public class Step1YouDTO {
    // --- INPUT ---
    private String planName;
    private Integer currentAge;
    private String gender;
    private Integer retirementAge = 60; // Default value

    // --- OUTPUT ---
    private Integer yearsToRetirement;
}
