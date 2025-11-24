package com.finplanpro.finplanpro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "insurance_policies")
public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String policyNumber; // เลขกรมธรรม์

    private String policyName; // ชื่อกรมธรรม์

    // Premiums
    private BigDecimal mainPremium = BigDecimal.ZERO; // เบี้ยสัญญาหลัก/ปี
    private BigDecimal riderPremium = BigDecimal.ZERO; // เบี้ยสัญญาเพิ่มเติม/ปี

    // Coverage Details
    private BigDecimal lifeCoverage = BigDecimal.ZERO; // ทุนคุ้มครองชีวิต
    private BigDecimal disabilityCoverage = BigDecimal.ZERO; // คุ้มครองทุพพลภาพ
    private BigDecimal earlyMidCriticalIllness = BigDecimal.ZERO; // โรคร้ายแรงต้น-กลาง
    private BigDecimal severeCriticalIllness = BigDecimal.ZERO; // โรคร้ายแรงระยะรุนแรง
    private BigDecimal partialAccidentCompensation = BigDecimal.ZERO; // ชดเชยอุบัติเหตุบางส่วน/สัปดาห์
    private BigDecimal accidentCoverage = BigDecimal.ZERO; // คุ้มครองจากอุบัติเหตุรวม(PA)
    private BigDecimal healthCareRoom = BigDecimal.ZERO; // ค่าห้องรวม
    private BigDecimal healthCarePerVisit = BigDecimal.ZERO; // ค่ารักษา/ครั้ง

    // Not requested in the list, but keeping them for data integrity
    private BigDecimal savingsReturn = BigDecimal.ZERO;
    private BigDecimal pension = BigDecimal.ZERO;
    private BigDecimal unitLinkedBenefits = BigDecimal.ZERO;
    private BigDecimal opdPerVisit = BigDecimal.ZERO;
    private BigDecimal compensationPerDay = BigDecimal.ZERO;


    @Transient
    public BigDecimal getTotalPremium() {
        if (mainPremium == null) mainPremium = BigDecimal.ZERO;
        if (riderPremium == null) riderPremium = BigDecimal.ZERO;
        return mainPremium.add(riderPremium);
    }
}
