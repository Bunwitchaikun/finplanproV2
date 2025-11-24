package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicCalculatorServiceTest {

    private BasicCalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        // เราสร้าง instance ของ service โดยตรง เพราะมันไม่มี dependencies
        calculatorService = new BasicCalculatorService();
    }

    @Test
    @DisplayName("คำนวณเงินที่ต้องมี ณ วันเกษียณ (Total Funds Needed) กรณีทั่วไป")
    void testCalculateTotalRetirementFund_HappyPath() {
        // 1. Arrange - เตรียมข้อมูลทดสอบ
        RetirementBasic plan = new RetirementBasic();
        plan.setCurrentAge(30);
        plan.setRetireAge(60);
        plan.setLifeExpectancy(90);
        plan.setMonthlyExpense(new BigDecimal("30000"));
        plan.setInflationRate(3.0); // 3%
        plan.setPostRetireReturn(5.0); // 5%

        // 2. Act - เรียกเมธอดที่ต้องการทดสอบ
        BigDecimal totalFundsNeeded = calculatorService.calculateTotalRetirementFund(plan);

        // 3. Assert - ตรวจสอบผลลัพธ์
        // ค่าที่คาดหวังจากการคำนวณด้วยเครื่องคิดเลขการเงิน:
        // FV of monthly expense: 30000 * (1.03^30) = 72,817.87
        // Annual expense at retirement: 72,817.87 * 12 = 873,814.44
        // Real rate of return: ((1.05 / 1.03) - 1) = 1.9417%
        // PV of annuity: PV(1.9417%, 30, -873814.44) = 19,784,891.41
        BigDecimal expected = new BigDecimal("19784891.41");

        // เปรียบเทียบโดยกำหนดทศนิยม 2 ตำแหน่ง
        assertEquals(expected, totalFundsNeeded.setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    @DisplayName("คำนวณเงินที่ต้องมี ณ วันเกษียณ เมื่อผลตอบแทนหลังเกษียณเท่ากับเงินเฟ้อ")
    void testCalculateTotalRetirementFund_ZeroRealReturn() {
        // 1. Arrange
        RetirementBasic plan = new RetirementBasic();
        plan.setCurrentAge(30);
        plan.setRetireAge(60);
        plan.setLifeExpectancy(90);
        plan.setMonthlyExpense(new BigDecimal("30000"));
        plan.setInflationRate(3.0);
        plan.setPostRetireReturn(3.0); // เท่ากับเงินเฟ้อ

        // 2. Act
        BigDecimal totalFundsNeeded = calculatorService.calculateTotalRetirementFund(plan);

        // 3. Assert
        // ค่าที่คาดหวัง: (ค่าใช้จ่ายปีแรก ณ วันเกษียณ) * (จำนวนปีหลังเกษียณ)
        // FV of monthly expense: 30000 * (1.03^30) = 72,817.87
        // Annual expense at retirement: 72,817.87 * 12 = 873,814.44
        // Total needed: 873,814.44 * 30 = 26,214,433.20
        BigDecimal expected = new BigDecimal("26214433.20");

        assertEquals(expected, totalFundsNeeded.setScale(2, RoundingMode.HALF_UP));
    }
}
