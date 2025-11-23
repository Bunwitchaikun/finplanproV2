package com.finplanpro.finplanpro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceSummaryDto {
    // Coverage totals - 13 fields
    private BigDecimal totalLifeCoverage; // ทุนคุ้มครองชีวิต
    private BigDecimal totalDisabilityCoverage; // คุ้มครองทุพพลภาพ
    private BigDecimal totalAccidentCoverage; // คุ้มครองจากอุบัติเหตุรวม(PA)
    private BigDecimal totalSavingsReturn; // เงินคืนสะสมทรัพย์รวม
    private BigDecimal totalPension; // เงินบำนาญรวม
    private BigDecimal totalUnitLinkedBenefits; // ผลประโยชน์ Unit Linked
    private BigDecimal totalHealthCareRoom; // ค่าห้องรวม
    private BigDecimal totalHealthCarePerVisit; // ค่ารักษา/ครั้ง
    private BigDecimal totalOpdPerVisit; // ผู้ป่วยนอก/ครั้ง(OPD)
    private BigDecimal totalCompensationPerDay; // ชดเชย/วัน
    private BigDecimal totalEarlyMidCriticalIllness; // โรคร้ายแรงต้น-กลาง
    private BigDecimal totalSevereCriticalIllness; // โรคร้ายแรงระยะรุนแรง
    private BigDecimal totalPartialAccidentCompensation; // ชดเชยอุบัติเหตุบางส่วน/สัปดาห์

    // Premium totals - 2 fields
    private BigDecimal totalMainPremium; // รวมเบี้ยสัญญาหลัก/ปี
    private BigDecimal totalRiderPremium; // รวมเบี้ยสัญญาเพิ่มเติม/ปี

    public BigDecimal getTotalPremium() {
        return totalMainPremium.add(totalRiderPremium);
    }
}
