package com.finplanpro.finplanpro.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO สำหรับ Step 5: ทุนเกษียณที่มีอยู่และที่จะได้รับในอนาคต
 */
@Data
public class Step5HavesDTO {

    // ส่วนที่ 1: ทุนเกษียณที่คุณมี (ปัจจุบัน) - ดึงจาก D5
    private List<CurrentAssetItem> currentAssets = new ArrayList<>();

    // ส่วนที่ 2: ทุนเกษียณที่คาดว่าจะได้รับ (อนาคต)
    private FutureIncome futureIncome = new FutureIncome();
    private List<FutureAssetItem> futureAssets = new ArrayList<>();

    // --- OUTPUT ---
    private BigDecimal totalCurrentAssetsFV;  // รวมทุนเกษียณที่คุณมี (ปัจจุบัน)
    private BigDecimal totalFutureAssetsFV;   // รวมทุนเกษียณที่คาดว่าจะได้รับ (อนาคต)
    private BigDecimal totalHavesFV;          // รวมทุนเกษียณทั้งหมด

    /**
     * Class สำหรับรายการสินทรัพย์ปัจจุบันแต่ละประเภท
     */
    @Data
    public static class CurrentAssetItem {
        private String name;
        private BigDecimal presentValue = BigDecimal.ZERO;
        private BigDecimal expectedReturnRate = BigDecimal.ZERO; // % per year
    }

    /**
     * Class สำหรับรายการเงินได้ในอนาคต (แบบคงที่)
     */
    @Data
    public static class FutureIncome {
        private BigDecimal gratuity = BigDecimal.ZERO; // บำเหน็จ
        private BigDecimal socialSecurityPension = BigDecimal.ZERO; // บำนาญจากประกันสังคม
        private BigDecimal providentFund = BigDecimal.ZERO; // กองทุนสำรองเลี้ยงชีพ
        private BigDecimal annuityInsurance = BigDecimal.ZERO; // ประกันบำนาญ
        private BigDecimal lifeInsuranceMaturity = BigDecimal.ZERO; // เงินครบกำหนดประกันชีวิต
        private BigDecimal realEstateForSale = BigDecimal.ZERO; // อสังหาริมทรัพย์ (ถ้าขาย)
    }

    /**
     * Class สำหรับรายการสินทรัพย์ในอนาคต (ที่ผู้ใช้เพิ่มเอง)
     */
    @Data
    public static class FutureAssetItem {
        private String name;
        private BigDecimal amount = BigDecimal.ZERO;
        private BigDecimal expectedReturnRate = BigDecimal.ZERO; // % per year
    }
}
