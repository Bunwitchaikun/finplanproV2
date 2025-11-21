package com.finplanpro.finplanpro.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * DTO สำหรับ Step 1: ข้อมูลเกี่ยวกับตัวคุณ
 */
@Data
public class Step1YouDTO {
    // --- INPUT ---
    private LocalDate dateOfBirth; // วันเดือนปีเกิด
    private String gender;         // เพศ
    private int retireMonth;       // เดือนที่ต้องการเกษียณ
    private int retireYear;        // ปีที่ต้องการเกษียณ

    // --- OUTPUT ---
    private int currentAge;        // อายุปัจจุบัน
    private int retirementAge;     // อายุ ณ วันเกษียณ
    private int yearsToRetirement; // จำนวนปีที่เหลือก่อนเกษียณ
}
