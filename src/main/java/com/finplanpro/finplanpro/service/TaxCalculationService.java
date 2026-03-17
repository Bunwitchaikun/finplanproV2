package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.TaxRequestDTO;
import com.finplanpro.finplanpro.dto.TaxResultDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
public class TaxCalculationService {

    public TaxResultDTO calculateTax(TaxRequestDTO dto) {
        double monthlyIncomeValue = Optional.ofNullable(dto.getMonthlyIncome()).orElse(0.0);
        double customExpenseValue = Optional.ofNullable(dto.getCustomExpense()).orElse(0.0);

        BigDecimal annualIncome = BigDecimal.valueOf(monthlyIncomeValue).multiply(BigDecimal.valueOf(12));

        BigDecimal expenseDeduction;
        // ... (ส่วนนี้ถูกต้องแล้ว)
        switch (dto.getIncomeType()) {
            case "เงินได้จากเงินเดือน โบนัส ค่าล่วงเวลา (Salary)":
            case "เงินได้จากค่านายหน้า รับจ้างทำงาน (Commision)":
            case "เงินได้จากค่าลิขสิทธิ์ สิทธิบัตร (Goodwill)":
                expenseDeduction = annualIncome.multiply(new BigDecimal("0.5")).min(new BigDecimal("100000"));
                break;
            case "เงินได้จากค่าเช่าทรัพย์สิน (Rent)":
                expenseDeduction = annualIncome.multiply(new BigDecimal("0.3")).min(new BigDecimal("100000"));
                break;
            case "เงินได้จากการเป็นวิชาชีพอิสระ (Profession)":
            case "เงินได้จากการรับเหมา (Constructor)":
            case "เงินได้อื่นๆ (Others)":
                expenseDeduction = annualIncome.multiply(new BigDecimal("0.6")).min(new BigDecimal("100000"));
                break;
            default:
                expenseDeduction = BigDecimal.ZERO;
                break;
        }
        BigDecimal totalExpenseDeduction = expenseDeduction.add(BigDecimal.valueOf(customExpenseValue));

        // --- เริ่มการคำนวณค่าลดหย่อน ---
        BigDecimal totalAllowance = BigDecimal.ZERO;
        totalAllowance = totalAllowance.add(new BigDecimal("60000")); // personal
        if (dto.isSpouse()) {
            totalAllowance = totalAllowance.add(new BigDecimal("60000"));
        }
        totalAllowance = totalAllowance.add(new BigDecimal("30000").multiply(BigDecimal.valueOf(dto.getChildren())));
        if (dto.getChildbirth() > 0) {
            totalAllowance = totalAllowance.add(new BigDecimal("60000"));
        }
        totalAllowance = totalAllowance.add(new BigDecimal("30000").multiply(BigDecimal.valueOf(dto.getParents())));
        if (dto.isDisability()) {
            totalAllowance = totalAllowance.add(new BigDecimal("60000"));
        }
        if (dto.isHealthInsuranceParents()) {
            totalAllowance = totalAllowance.add(new BigDecimal("15000"));
        }

        // ประกันชีวิต + ประกันสุขภาพตนเอง (รวมกันไม่เกิน 100,000)
        BigDecimal lifeAndHealthInsurance = BigDecimal.ZERO;
        if (dto.isLifeInsurance()) {
            lifeAndHealthInsurance = lifeAndHealthInsurance.add(new BigDecimal("100000")); // สมมติว่าจ่ายเต็ม
        }
        if (dto.isHealthInsuranceSelf()) {
            lifeAndHealthInsurance = lifeAndHealthInsurance.add(new BigDecimal("25000")); // สมมติว่าจ่ายเต็ม
        }
        totalAllowance = totalAllowance.add(lifeAndHealthInsurance.min(new BigDecimal("100000")));

        if (dto.isSocialSecurity()) {
            totalAllowance = totalAllowance.add(new BigDecimal("9000")); // ประกันสังคม (ไม่ใช่ กอช.)
        }

        // --- แก้ไข Logic การรวมยอดลดหย่อนกองทุน ---
        BigDecimal retirementFundDeduction = BigDecimal.ZERO;
        if (dto.isPension()) {
            // ประกันบำนาญ (ไม่เกิน 200,000 และไม่เกิน 15% ของเงินได้)
            retirementFundDeduction = retirementFundDeduction.add(annualIncome.multiply(new BigDecimal("0.15")).min(new BigDecimal("200000")));
        }
        if (dto.isProvidentFund()) {
            // กองทุนสำรองเลี้ยงชีพ/กบข./สงเคราะห์ครู
            retirementFundDeduction = retirementFundDeduction.add(annualIncome.multiply(new BigDecimal("0.15")).min(new BigDecimal("500000")));
        }
        if (dto.isRmf()) {
            // RMF (ไม่เกิน 30% ของเงินได้)
            retirementFundDeduction = retirementFundDeduction.add(annualIncome.multiply(new BigDecimal("0.3")));
        }
        if (dto.isSsf()) {
            // SSF (ไม่เกิน 30% ของเงินได้ และไม่เกิน 200,000)
            retirementFundDeduction = retirementFundDeduction.add(annualIncome.multiply(new BigDecimal("0.3")).min(new BigDecimal("200000")));
        }
        // จำกัดเพดานรวมของกองทุนทั้งหมดที่ 500,000 บาท
        totalAllowance = totalAllowance.add(retirementFundDeduction.min(new BigDecimal("500000")));
        // --- สิ้นสุดการแก้ไข ---

        BigDecimal netIncome = annualIncome.subtract(totalAllowance).subtract(totalExpenseDeduction);
        if (netIncome.compareTo(BigDecimal.ZERO) < 0) {
            netIncome = BigDecimal.ZERO;
        }

        BigDecimal taxAmount = calculateProgressiveTax(netIncome);

        return new TaxResultDTO(annualIncome, totalExpenseDeduction, totalAllowance, netIncome, taxAmount);
    }

    private BigDecimal calculateProgressiveTax(BigDecimal netIncome) {
        // ... (ส่วนนี้ถูกต้องแล้ว)
        if (netIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal remainingIncome = netIncome;

        // 0 - 150,000
        remainingIncome = remainingIncome.subtract(new BigDecimal("150000"));
        if (remainingIncome.compareTo(BigDecimal.ZERO) <= 0) return tax;

        // 150,001 - 300,000
        BigDecimal bracket2 = remainingIncome.min(new BigDecimal("150000"));
        tax = tax.add(bracket2.multiply(new BigDecimal("0.05")));
        remainingIncome = remainingIncome.subtract(bracket2);
        if (remainingIncome.compareTo(BigDecimal.ZERO) <= 0) return tax.setScale(2, RoundingMode.HALF_UP);

        // 300,001 - 500,000
        BigDecimal bracket3 = remainingIncome.min(new BigDecimal("200000"));
        tax = tax.add(bracket3.multiply(new BigDecimal("0.10")));
        remainingIncome = remainingIncome.subtract(bracket3);
        if (remainingIncome.compareTo(BigDecimal.ZERO) <= 0) return tax.setScale(2, RoundingMode.HALF_UP);

        // 500,001 - 750,000
        BigDecimal bracket4 = remainingIncome.min(new BigDecimal("250000"));
        tax = tax.add(bracket4.multiply(new BigDecimal("0.15")));
        remainingIncome = remainingIncome.subtract(bracket4);
        if (remainingIncome.compareTo(BigDecimal.ZERO) <= 0) return tax.setScale(2, RoundingMode.HALF_UP);

        // 750,001 - 1,000,000
        BigDecimal bracket5 = remainingIncome.min(new BigDecimal("250000"));
        tax = tax.add(bracket5.multiply(new BigDecimal("0.20")));
        remainingIncome = remainingIncome.subtract(bracket5);
        if (remainingIncome.compareTo(BigDecimal.ZERO) <= 0) return tax.setScale(2, RoundingMode.HALF_UP);

        // 1,000,001 - 2,000,000
        BigDecimal bracket6 = remainingIncome.min(new BigDecimal("1000000"));
        tax = tax.add(bracket6.multiply(new BigDecimal("0.25")));
        remainingIncome = remainingIncome.subtract(bracket6);
        if (remainingIncome.compareTo(BigDecimal.ZERO) <= 0) return tax.setScale(2, RoundingMode.HALF_UP);

        // 2,000,001 - 5,000,000
        BigDecimal bracket7 = remainingIncome.min(new BigDecimal("3000000"));
        tax = tax.add(bracket7.multiply(new BigDecimal("0.30")));
        remainingIncome = remainingIncome.subtract(bracket7);
        if (remainingIncome.compareTo(BigDecimal.ZERO) <= 0) return tax.setScale(2, RoundingMode.HALF_UP);

        // > 5,000,000
        tax = tax.add(remainingIncome.multiply(new BigDecimal("0.35")));

        return tax.setScale(2, RoundingMode.HALF_UP);
    }
}
