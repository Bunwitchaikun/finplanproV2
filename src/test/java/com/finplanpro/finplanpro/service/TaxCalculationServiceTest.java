package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.TaxRequestDTO;
import com.finplanpro.finplanpro.dto.TaxResultDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TaxCalculationServiceTest {

    @InjectMocks
    private TaxCalculationService taxCalculationService;

    @Test
    @DisplayName("คำนวณภาษี กรณีทั่วไป (เงินเดือน 1 แสน, ลดหย่อนเต็มจำนวน)")
    void testCalculateTax_HappyPath() {
        System.out.println("--- RUNNING: [D7] testCalculateTax_HappyPath ---");
        // 1. Arrange
        TaxRequestDTO request = new TaxRequestDTO();
        request.setMonthlyIncome(100000.0); // 1.2M per year
        request.setIncomeType("เงินได้จากเงินเดือน โบนัส ค่าล่วงเวลา (Salary)");
        request.setSpouse(true);
        request.setChildren(2);
        request.setParents(2);
        request.setLifeInsurance(true);
        request.setSsf(true);
        request.setRmf(true);
        request.setProvidentFund(true);
        request.setSocialSecurity(true);

        System.out.println("INPUT: " + request);

        // 2. Act
        TaxResultDTO result = taxCalculationService.calculateTax(request);
        System.out.println("ACTUAL_RESULT: NetIncome=" + result.getNetIncome() + ", Tax=" + result.getTaxAmount());

        // 3. Assert
        // Calculation based on CORRECTED logic:
        // Income: 1,200,000
        // Expenses: 100,000
        // Allowances:
        //   Personal: 60,000
        //   Spouse: 60,000
        //   Children: 2 * 30,000 = 60,000
        //   Parents: 2 * 30,000 = 60,000
        //   Social Security: 9,000
        //   Life Insurance: 100,000
        //   Retirement Funds (PVD+RMF+SSF): capped at 500,000
        // Total Allowance: 60k+60k+60k+60k+9k+100k+500k = 849,000
        // Total Deductions: 100,000 (Expenses) + 849,000 (Allowances) = 949,000
        // Net Income: 1,200,000 - 949,000 = 251,000
        // Tax: (150,000 * 0%) + (101,000 * 5%) = 5,050
        BigDecimal expectedNetIncome = new BigDecimal("251000.00");
        BigDecimal expectedTax = new BigDecimal("5050.00");
        System.out.println("EXPECTED_RESULT: NetIncome=" + expectedNetIncome + ", Tax=" + expectedTax);

        assertEquals(0, expectedNetIncome.compareTo(result.getNetIncome().setScale(2, RoundingMode.HALF_UP)), "Net income should be correct.");
        assertEquals(0, expectedTax.compareTo(result.getTaxAmount()), "Tax payable should be correct.");
        System.out.println("✅ SUCCESS: Tax calculation is correct.");
        System.out.println("--- FINISHED: [D7] testCalculateTax_HappyPath ---\n");
    }

    @Test
    @DisplayName("คำนวณภาษี กรณีรายได้น้อย ไม่ต้องเสียภาษี")
    void testCalculateTax_NoTaxPayable() {
        System.out.println("--- RUNNING: [D7] testCalculateTax_NoTaxPayable ---");
        // 1. Arrange
        TaxRequestDTO request = new TaxRequestDTO();
        request.setMonthlyIncome(25000.0); // 300k per year
        request.setSocialSecurity(true);
        request.setParents(0); // <<-- เพิ่มบรรทัดนี้เพื่อลบล้างค่า Default

        System.out.println("INPUT: " + request);

        // 2. Act
        TaxResultDTO result = taxCalculationService.calculateTax(request);
        System.out.println("ACTUAL_RESULT: NetIncome=" + result.getNetIncome() + ", Tax=" + result.getTaxAmount());

        // 3. Assert
        // Calculation:
        // Income: 300,000
        // Expenses: 100,000
        // Allowances: Personal(60k) + SocialSec(9k) = 69,000
        // Total Deductions: 100,000 + 69,000 = 169,000
        // Net Income: 300,000 - 169,000 = 131,000
        // Tax: (131,000 * 0%) = 0
        BigDecimal expectedNetIncome = new BigDecimal("131000.00");
        BigDecimal expectedTax = new BigDecimal("0.00");
        System.out.println("EXPECTED_RESULT: NetIncome=" + expectedNetIncome + ", Tax=" + expectedTax);

        assertEquals(0, expectedNetIncome.compareTo(result.getNetIncome().setScale(2, RoundingMode.HALF_UP)), "Net income should be correct.");
        assertEquals(0, expectedTax.compareTo(result.getTaxAmount()), "Tax payable should be zero.");
        System.out.println("✅ SUCCESS: Correctly calculated zero tax payable.");
        System.out.println("--- FINISHED: [D7] testCalculateTax_NoTaxPayable ---\n");
    }
}
