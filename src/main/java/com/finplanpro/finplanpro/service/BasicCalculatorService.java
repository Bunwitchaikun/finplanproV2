package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class BasicCalculatorService {

    public BigDecimal calculateTotalRetirementFund(RetirementBasic retirementBasic) {
        int yearsToRetirement = retirementBasic.getRetireAge() - retirementBasic.getCurrentAge();
        
        BigDecimal inflationRate = BigDecimal.valueOf(retirementBasic.getInflationRate() / 100);
        
        // Calculate future monthly expense
        BigDecimal futureMonthlyExpense = retirementBasic.getMonthlyExpense()
                .multiply(BigDecimal.ONE.add(inflationRate).pow(yearsToRetirement));

        int retirementYears = retirementBasic.getLifeExpectancy() - retirementBasic.getRetireAge();
        BigDecimal postRetireReturnRate = BigDecimal.valueOf(retirementBasic.getPostRetireReturn() / 100);
        
        // Using Present Value of an Annuity formula for more accuracy
        // Real rate of return after inflation
        BigDecimal realReturnRate = (BigDecimal.ONE.add(postRetireReturnRate))
                                    .divide(BigDecimal.ONE.add(inflationRate), 16, RoundingMode.HALF_UP)
                                    .subtract(BigDecimal.ONE);

        BigDecimal totalRetirementFund;
        if (realReturnRate.compareTo(BigDecimal.ZERO) == 0) {
            totalRetirementFund = futureMonthlyExpense.multiply(BigDecimal.valueOf(retirementYears * 12L));
        } else {
            // PV = PMT * [1 - (1 + r)^-n] / r
            BigDecimal n = BigDecimal.valueOf(retirementYears * 12);
            BigDecimal r = realReturnRate.divide(BigDecimal.valueOf(12), 16, RoundingMode.HALF_UP); // monthly real return
            
            BigDecimal pvFactorNumerator = BigDecimal.ONE.subtract(BigDecimal.ONE.add(r).pow(-n.intValue()));
            BigDecimal pvFactor = pvFactorNumerator.divide(r, 16, RoundingMode.HALF_UP);
            
            totalRetirementFund = futureMonthlyExpense.multiply(pvFactor);
        }

        return totalRetirementFund.setScale(2, RoundingMode.HALF_UP);
    }
}
