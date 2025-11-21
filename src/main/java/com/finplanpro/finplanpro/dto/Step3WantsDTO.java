package com.finplanpro.finplanpro.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO สำหรับ Step 3: ไลฟ์สไตล์และความต้องการหลังเกษียณ
 */
@Data
public class Step3WantsDTO {
    // --- INPUT ---
    private boolean wantTaxFreeCash;      // ต้องการเงินก้อนปลอดภาษีหรือไม่
    private boolean wantIncomeIncrease;     // ต้องการให้รายได้เพิ่มขึ้นหลังเกษียณหรือไม่
    private int incomeIncreaseUntilAge;   // ให้รายได้เพิ่มขึ้นถึงอายุเท่าไหร่
    private String lifestyleLevel;        // ระดับไลฟ์สไตล์ (minimum, moderate, comfortable)
    private BigDecimal afterTaxIncome;      // รายได้หลังหักภาษีที่ต้องการต่อเดือน
    private BigDecimal extraIncome;         // รายได้เสริมพิเศษต่อเดือน
    private int extraIncomeYears;         // จำนวนปีที่จะมีรายได้เสริม

    // --- OUTPUT ---
    private List<IncomeProjection> incomeProjection; // การคาดการณ์รายได้ในแต่ละปีหลังเกษียณ

    @Data
    public static class IncomeProjection {
        private int year;
        private int age;
        private BigDecimal projectedIncome;
    }
}
