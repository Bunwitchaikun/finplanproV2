package com.finplanpro.finplanpro.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO สำหรับ Step 5: ทุนเกษียณที่มีอยู่
 */
@Data
public class Step5HavesDTO {
    // --- INPUT ---
    private List<AssetItem> assets;

    // --- OUTPUT ---
    private BigDecimal totalAssetsFV; // มูลค่ารวมของสินทรัพย์ทั้งหมด ณ วันเกษียณ

    @Data
    public static class AssetItem {
        private String assetName;      // ชื่อสินทรัพย์
        private BigDecimal valueToday;    // มูลค่าปัจจุบัน
        private BigDecimal returnRate;    // อัตราผลตอบแทนคาดหวัง
        private BigDecimal futureValue;   // (Output) มูลค่าในอนาคตของสินทรัพย์นี้
    }
}
