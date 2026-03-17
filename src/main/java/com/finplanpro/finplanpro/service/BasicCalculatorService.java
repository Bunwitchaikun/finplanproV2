package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class BasicCalculatorService {

    private static final int FINAL_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public BigDecimal calculateTotalRetirementFund(RetirementBasic retirementBasic) {
        int yearsToRetirement = retirementBasic.getRetireAge() - retirementBasic.getCurrentAge();
        int retirementYears = retirementBasic.getLifeExpectancy() - retirementBasic.getRetireAge();

        if (yearsToRetirement < 0 || retirementYears <= 0) {
            return BigDecimal.ZERO;
        }

        double inflationRate = retirementBasic.getInflationRate() / 100.0;
        double postRetireReturnRate = retirementBasic.getPostRetireReturn() / 100.0;
        BigDecimal monthlyExpense = retirementBasic.getMonthlyExpense();

        // 1. คำนวณค่าใช้จ่ายรายปี ณ วันเกษียณ (ปรับด้วยเงินเฟ้อ)
        // ใช้ Math.pow() เพื่อความแม่นยำที่ใกล้เคียงกับเครื่องคิดเลขการเงิน
        double futureValueFactor = Math.pow(1 + inflationRate, yearsToRetirement);
        BigDecimal annualExpenseAtRetirement = monthlyExpense
                .multiply(BigDecimal.valueOf(12))
                .multiply(BigDecimal.valueOf(futureValueFactor));

        // 2. คำนวณอัตราผลตอบแทนที่แท้จริงต่อปี
        double realReturnRate = ((1 + postRetireReturnRate) / (1 + inflationRate)) - 1;

        BigDecimal totalRetirementFund;

        // 3. คำนวณเงินก้อนที่ต้องมี
        // กรณีที่ผลตอบแทนที่แท้จริงเป็น 0
        if (Math.abs(realReturnRate) < 1e-9) {
            totalRetirementFund = annualExpenseAtRetirement.multiply(BigDecimal.valueOf(retirementYears));
        } else {
            // คำนวณด้วยสูตร Present Value of an Annuity
            // PV = PMT * [1 - (1 + r)^-n] / r
            double pvFactor = (1 - Math.pow(1 + realReturnRate, -retirementYears)) / realReturnRate;
            totalRetirementFund = annualExpenseAtRetirement.multiply(BigDecimal.valueOf(pvFactor));
        }

        return totalRetirementFund.setScale(FINAL_SCALE, ROUNDING_MODE);
    }
}
