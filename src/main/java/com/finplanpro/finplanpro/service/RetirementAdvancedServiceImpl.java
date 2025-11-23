package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.*;
import com.finplanpro.finplanpro.service.calculation.FinancialCalculator;
import com.finplanpro.finplanpro.service.calculation.ScenarioSimulator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RetirementAdvancedServiceImpl implements RetirementAdvancedService {

    private final FinancialCalculator financialCalculator;
    private final ScenarioSimulator scenarioSimulator;

    @Override
    public Step1YouDTO calculateStep1(Step1YouDTO input) {
        // Simple calculation: years to retirement = retirement age - current age
        if (input.getCurrentAge() != null && input.getRetirementAge() != null) {
            int yearsToRetirement = input.getRetirementAge() - input.getCurrentAge();
            input.setYearsToRetirement(yearsToRetirement);
        }
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
        // 1. Calculate Basic Expenses
        if (input.getBasicItems() != null && !input.getBasicItems().isEmpty()) {
            input.getBasicItems().forEach(item -> {
                if (item.getAmountToday() != null && item.getInflationRate() != null) {
                    // Convert percentage to decimal (e.g., 3.0 -> 0.03)
                    BigDecimal inflationDecimal = item.getInflationRate().divide(new BigDecimal("100"), 10,
                            RoundingMode.HALF_UP);
                    BigDecimal fv = financialCalculator.calculateFV(
                            item.getAmountToday(),
                            inflationDecimal,
                            yearsToRetirement);
                    item.setFutureValue(fv.setScale(2, RoundingMode.HALF_UP));
                } else {
                    item.setFutureValue(item.getAmountToday() != null ? item.getAmountToday() : BigDecimal.ZERO);
                }
            });

            BigDecimal totalBasicToday = input.getBasicItems().stream()
                    .map(item -> item.getAmountToday() != null ? item.getAmountToday() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            input.setTotalBasicExpensesToday(totalBasicToday);

            BigDecimal totalBasicFV = input.getBasicItems().stream()
                    .map(item -> item.getFutureValue() != null ? item.getFutureValue() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            input.setTotalBasicExpensesFV(totalBasicFV);
        } else {
            input.setTotalBasicExpensesToday(BigDecimal.ZERO);
            input.setTotalBasicExpensesFV(BigDecimal.ZERO);
        }

        // 2. Calculate Special Expenses
        if (input.getSpecialItems() != null && !input.getSpecialItems().isEmpty()) {
            input.getSpecialItems().forEach(item -> {
                if (item.getAmountToday() != null && item.getInflationRate() != null) {
                    // Convert percentage to decimal (e.g., 3.0 -> 0.03)
                    BigDecimal inflationDecimal = item.getInflationRate().divide(new BigDecimal("100"), 10,
                            RoundingMode.HALF_UP);
                    BigDecimal fv = financialCalculator.calculateFV(
                            item.getAmountToday(),
                            inflationDecimal,
                            yearsToRetirement);
                    item.setFutureValue(fv.setScale(2, RoundingMode.HALF_UP));
                } else {
                    item.setFutureValue(item.getAmountToday() != null ? item.getAmountToday() : BigDecimal.ZERO);
                }
            });

            BigDecimal totalSpecialToday = input.getSpecialItems().stream()
                    .map(item -> item.getAmountToday() != null ? item.getAmountToday() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            input.setTotalSpecialExpensesToday(totalSpecialToday);

            BigDecimal totalSpecialFV = input.getSpecialItems().stream()
                    .map(item -> item.getFutureValue() != null ? item.getFutureValue() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            input.setTotalSpecialExpensesFV(totalSpecialFV);
        } else {
            input.setTotalSpecialExpensesToday(BigDecimal.ZERO);
            input.setTotalSpecialExpensesFV(BigDecimal.ZERO);
        }

        // 3. Summary
        BigDecimal totalToday = (input.getTotalBasicExpensesToday() != null ? input.getTotalBasicExpensesToday()
                : BigDecimal.ZERO)
                .add(input.getTotalSpecialExpensesToday() != null ? input.getTotalSpecialExpensesToday()
                        : BigDecimal.ZERO);
        input.setTotalRetirementExpensesToday(totalToday);

        BigDecimal totalFV = (input.getTotalBasicExpensesFV() != null ? input.getTotalBasicExpensesFV()
                : BigDecimal.ZERO)
                .add(input.getTotalSpecialExpensesFV() != null ? input.getTotalSpecialExpensesFV() : BigDecimal.ZERO);
        input.setTotalRetirementExpensesFV(totalFV);

        return input;
    }

    @Override
    public Step5HavesDTO calculateAssetsFV(Step5HavesDTO input, int yearsToRetirement) {
        input.getAssets().forEach(asset -> {
            BigDecimal fv = financialCalculator.calculateFV(
                    asset.getValueToday(),
                    asset.getReturnRate(),
                    yearsToRetirement);
            asset.setFutureValue(fv.setScale(2, RoundingMode.HALF_UP));
        });
        BigDecimal totalFv = input.getAssets().stream()
                .map(Step5HavesDTO.AssetItem::getFutureValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        input.setTotalAssetsFV(totalFv);
        return input;
    }

    @Override
    public AssetLiabilityDTO calculateAssetsLiabilities(AssetLiabilityDTO input) {
        // Sum assets
        if (input.getAssetItems() != null) {
            BigDecimal totalAssets = input.getAssetItems().stream()
                    .map(AssetLiabilityDTO.Item::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            input.setTotalAssets(totalAssets);
        } else {
            input.setTotalAssets(BigDecimal.ZERO);
        }

        // Sum liabilities
        if (input.getLiabilityItems() != null) {
            BigDecimal totalLiabilities = input.getLiabilityItems().stream()
                    .map(AssetLiabilityDTO.Item::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            input.setTotalLiabilities(totalLiabilities);
        } else {
            input.setTotalLiabilities(BigDecimal.ZERO);
        }

        // Net worth
        BigDecimal netWorth = input.getTotalAssets().subtract(input.getTotalLiabilities());
        input.setNetWorth(netWorth);

        return input;
    }

    @Override
    public DesignResultDTO calculateDesignGap(BigDecimal monthlyCostToday, BigDecimal inflation, int yearsToRetirement,
            int yearsAfterRetirement, BigDecimal returnBeforeRetirement, BigDecimal returnAfterRetirement,
            BigDecimal totalAssetsFV, BigDecimal totalSpecialExpensesFV) {

        BigDecimal monthlyCostAtRetirement = financialCalculator.calculateFV(monthlyCostToday, inflation,
                yearsToRetirement);

        BigDecimal r = returnAfterRetirement;
        BigDecimal targetFund = financialCalculator
                .calculatePV(monthlyCostAtRetirement.multiply(BigDecimal.valueOf(12)), r, yearsAfterRetirement);

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
