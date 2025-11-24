package com.finplanpro.finplanpro.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO สำหรับ Step 7: ผลการทดสอบ Stress Test
 */
@Data
@Builder
public class ScenarioResultDTO {
    private String scenarioName;        // ชื่อสถานการณ์จำลอง
    private BigDecimal scenarioTargetFund;  // เงินทุนเป้าหมายในสถานการณ์นั้น
    private BigDecimal scenarioGap;         // เงินทุนที่ขาด/เกินในสถานการณ์นั้น
    private boolean success;              // แผนยังคงสำเร็จหรือไม่
    private String description;           // คำอธิบายผล
}
