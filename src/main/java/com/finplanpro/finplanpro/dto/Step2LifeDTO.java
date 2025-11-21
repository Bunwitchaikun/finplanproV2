package com.finplanpro.finplanpro.dto;

import lombok.Data;

/**
 * DTO สำหรับ Step 2: การประเมินอายุขัย
 */
@Data
public class Step2LifeDTO {
    // --- INPUT ---
    private String healthLevel; // ระดับสุขภาพ (perfect, minor, moderate, major, unknown)

    // --- OUTPUT ---
    private int lifeExpectancy;       // อายุขัยคาดการณ์
    private int yearsAfterRetirement; // จำนวนปีหลังเกษียณ
}
