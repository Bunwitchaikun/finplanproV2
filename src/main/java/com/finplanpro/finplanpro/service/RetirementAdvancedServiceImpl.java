package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.*;
import com.finplanpro.finplanpro.service.calculation.FinancialCalculator;
import com.finplanpro.finplanpro.service.calculation.ScenarioSimulator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RetirementAdvancedServiceImpl implements RetirementAdvancedService {

    private final FinancialCalculator financialCalculator;
    private final ScenarioSimulator scenarioSimulator;

    @Override
    public Step1YouDTO calculateStep1(Step1YouDTO input) {
        LocalDate today = LocalDate.now();
        LocalDate retirementDate = LocalDate.of(input.getRetireYear(), input.getRetireMonth(), 1);

        int currentAge = Period.between(input.getDateOfBirth(), today).getYears();
        int retirementAge = Period.between(input.getDateOfBirth(), retirementDate).getYears();
        int yearsToRetirement = retirementAge - currentAge;

        input.setCurrentAge(currentAge);
        input.setRetirementAge(retirementAge);
        input.setYearsToRetirement(yearsToRetirement);
        return input;
    }

    @Override
    public Step2LifeDTO calculateStep2(Step2LifeDTO input, int retirementAge) {
        int adjustment = switch (input.getHealthLevel()) {
            case "perfect" -> 3;
            case "minor" -> 1;
            case "moderate" -> -1;
            case "major" -> -3;
            default -> 0; // unknown
        };
        int lifeExpectancy = 90 + adjustment;
        input.setLifeExpectancy(lifeExpectancy);
        input.setYearsAfterRetirement(lifeExpectancy - retirementAge);
        return input;
    }

    @Override
    public Step3WantsDTO calculateStep3IncomeProjection(Step3WantsDTO input) {
        // This is a placeholder for a more complex projection logic
        return input;
    }

    @Override
    public Step4ExpenseDTO calculateSpecialExpensesFV(Step4ExpenseDTO input, int yearsToRetirement) {
        input.getItems().forEach(item -> {
            BigDecimal fv = financialCalculator.calculateFV(
                item.getAmountToday(),
                item.getInflationRate(),
                yearsToRetirement
            );
            item.setFutureValue(fv.setScale(2, RoundingMode.HALF_UP));
        });
        BigDecimal totalFv = input.getItems().stream()
                .map(Step4ExpenseDTO.SpecialExpenseItem::getFutureValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        input.setTotalSpecialExpensesFV(totalFv);
        return input;
    }

    @Override
    public Step5HavesDTO calculateAssetsFV(Step5HavesDTO input, int yearsToRetirement) {
        input.getAssets().forEach(asset -> {
            BigDecimal fv = financialCalculator.calculateFV(
                asset.getValueToday(),
                asset.getReturnRate(),
                yearsToRetirement
            );
            asset.setFutureValue(fv.setScale(2, RoundingMode.HALF_UP));
        });
        BigDecimal totalFv = input.getAssets().stream()
                .map(Step5HavesDTO.AssetItem::getFutureValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        input.setTotalAssetsFV(totalFv);
        return input;
    }

    @Override
    public DesignResultDTO calculateDesignGap(BigDecimal monthlyCostToday, BigDecimal inflation, int yearsToRetirement, int yearsAfterRetirement, BigDecimal returnBeforeRetirement, BigDecimal returnAfterRetirement, BigDecimal totalAssetsFV, BigDecimal totalSpecialExpensesFV) {
        
        BigDecimal monthlyCostAtRetirement = financialCalculator.calculateFV(monthlyCostToday, inflation, yearsToRetirement);

        BigDecimal r = returnAfterRetirement;
        BigDecimal targetFund = financialCalculator.calculatePV(monthlyCostAtRetirement.multiply(BigDecimal.valueOf(12)), r, yearsAfterRetirement);

        BigDecimal targetAll = targetFund.add(totalSpecialExpensesFV);

        BigDecimal gap = targetAll.subtract(totalAssetsFV);

        BigDecimal pmt = financialCalculator.calculatePMT(gap, returnBeforeRetirement, yearsToRetirement * 12);

        return DesignResultDTO.builder()
                .monthlyCostAtRetirement(monthlyCostAtRetirement.setScale(2, RoundingMode.HALF_UP))
                .targetFund(targetFund.setScale(2, RoundingMode.HALF_UP))
                .targetAll(targetAll.setScale(2, RoundingMode.HALF_UP))
                .gap(gap.setScale(2, RoundingMode.HALF_UP))
                .requiredMonthlyInvestment(pmt.setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    @Override
    public List<ScenarioResultDTO> runScenarios(DesignResultDTO baseDesign) {
        return scenarioSimulator.runScenarios(baseDesign);
    }
}
