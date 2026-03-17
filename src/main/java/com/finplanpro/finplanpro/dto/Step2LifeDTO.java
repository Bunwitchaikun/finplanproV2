package com.finplanpro.finplanpro.dto;

import lombok.Data;

/**
 * DTO สำหรับ Step 2: การประเมินอายุขัย
 */
@Data
public class Step2LifeDTO {
    // --- INPUT ---
    private String healthLevel = "moderate"; // Default value
    private int lifeExpectancy; // อายุขัยคาดการณ์ (ผู้ใช้สามารถแก้ไขได้)

    // --- OUTPUT ---
    private int yearsAfterRetirement; // จำนวนปีหลังเกษียณ
}
