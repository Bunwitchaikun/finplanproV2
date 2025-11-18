package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import org.springframework.stereotype.Service;

@Service
public class BasicCalculatorService {

    public double calculateTotalRetirementFund(RetirementBasic retirementBasic) {
        int yearsToRetirement = retirementBasic.getRetireAge() - retirementBasic.getCurrentAge();
        double futureMonthlyExpense = retirementBasic.getMonthlyExpense() * Math.pow(1 + retirementBasic.getInflationRate() / 100, yearsToRetirement);

        int retirementYears = retirementBasic.getLifeExpectancy() - retirementBasic.getRetireAge();
        double totalRetirementFund = 0;

        for (int i = 0; i < retirementYears * 12; i++) {
            totalRetirementFund += futureMonthlyExpense * Math.pow(1 + retirementBasic.getPostRetireReturn() / 100 / 12, -i);
        }

        return totalRetirementFund;
    }
}
