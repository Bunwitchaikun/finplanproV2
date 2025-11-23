package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.*;
import com.finplanpro.finplanpro.entity.NetWorthItem;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.service.calculation.FinancialCalculator;
import com.finplanpro.finplanpro.service.calculation.ScenarioSimulator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetirementAdvancedServiceImpl implements RetirementAdvancedService {

    private final FinancialCalculator financialCalculator;
    private final ScenarioSimulator scenarioSimulator;

    @Override
    public Step1YouDTO calculateStep1(Step1YouDTO input) {
        if (input.getCurrentAge() != null && input.getRetirementAge() != null) {
            int years = input.getRetirementAge() - input.getCurrentAge();
            input.setYearsToRetirement(Math.max(0, years));
        }
        return input;
    }

    @Override
    public Step2LifeDTO calculateStep2(Step2LifeDTO input, int retirementAge, String gender) {
        if (input.getLifeExpectancy() <= 0) {
            int baseExpectancy = "FEMALE".equalsIgnoreCase(gender) ? 80 : 75;
            int healthAdjustment = switch (input.getHealthLevel()) {
                case "perfect" -> 3;
                case "minor" -> 1;
                case "moderate" -> -1;
                case "major" -> -3;
                default -> 0;
            };
            input.setLifeExpectancy(baseExpectancy + healthAdjustment);
        }
        input.setYearsAfterRetirement(input.getLifeExpectancy() - retirementAge);
        return input;
    }

    @Override
    public Step4ExpenseDTO calculateSpecialExpensesFV(Step4ExpenseDTO input, int yearsToRetirement, int yearsAfterRetirement) {
        // --- Basic Items Calculation ---
        BigDecimal totalBasicToday = BigDecimal.ZERO;
        BigDecimal totalBasicFV = BigDecimal.ZERO; // This will be sum of monthly FV at retirement
        BigDecimal totalAnnualizedBasicToday = BigDecimal.ZERO;
        BigDecimal totalAnnualizedBasicFV = BigDecimal.ZERO; // This will be sum of annual FV at retirement

        if (input.getBasicItems() != null) {
            for (Step4ExpenseDTO.ExpenseItem item : input.getBasicItems()) {
                // For Basic Items, calculate FV based on monthly periods and monthly rate
                calculateItemFV(item, yearsToRetirement, true); // true for monthly
                BigDecimal amountToday = Optional.ofNullable(item.getAmountToday()).orElse(BigDecimal.ZERO);
                totalBasicToday = totalBasicToday.add(amountToday); // Sum of monthly amounts today
                totalBasicFV = totalBasicFV.add(item.getFutureValue()); // Sum of monthly FV at retirement

                // All basic items are now considered monthly, so annualize them for summary
                BigDecimal annualizedToday = amountToday.multiply(BigDecimal.valueOf(12));
                BigDecimal annualizedFV = item.getFutureValue().multiply(BigDecimal.valueOf(12));
                
                totalAnnualizedBasicToday = totalAnnualizedBasicToday.add(annualizedToday);
                totalAnnualizedBasicFV = totalAnnualizedBasicFV.add(annualizedFV);
            }
        }
        input.setTotalBasicExpensesToday(totalBasicToday);
        input.setTotalBasicExpensesFV(totalBasicFV); 

        // Corrected calculation for totalBasicExpensesUntilEndOfLife
        // It should be totalAnnualizedBasicFV (sum of annual FV at retirement) * yearsAfterRetirement
        BigDecimal totalUntilEndOfLife = totalAnnualizedBasicFV.multiply(BigDecimal.valueOf(yearsAfterRetirement));
        input.setTotalBasicExpensesUntilEndOfLife(totalUntilEndOfLife.setScale(2, RoundingMode.HALF_UP));


        // --- Special Items Calculation ---
        BigDecimal totalSpecialToday = BigDecimal.ZERO;
        BigDecimal totalSpecialFV = BigDecimal.ZERO;
        if (input.getSpecialItems() != null) {
            for (Step4ExpenseDTO.ExpenseItem item : input.getSpecialItems()) {
                // For Special Items, calculate FV based on annual periods and annual rate
                calculateItemFV(item, yearsToRetirement, false); // false for annual
                totalSpecialToday = totalSpecialToday.add(Optional.ofNullable(item.getAmountToday()).orElse(BigDecimal.ZERO));
                totalSpecialFV = totalSpecialFV.add(item.getFutureValue());
            }
        }
        input.setTotalSpecialExpensesToday(totalSpecialToday);
        input.setTotalSpecialExpensesFV(totalSpecialFV);

        // --- Grand Totals (New Formulas) ---
        // totalAnnualizedBasicToday is sum of (monthly_today * 12)
        BigDecimal todayTotal = (totalAnnualizedBasicToday.multiply(BigDecimal.valueOf(yearsAfterRetirement))).add(totalSpecialToday);
        input.setTotalRetirementExpensesToday(todayTotal.setScale(2, RoundingMode.HALF_UP));

        // totalUntilEndOfLife is sum of (monthly_fv * 12 * yearsAfterRetirement)
        BigDecimal fvTotal = totalUntilEndOfLife.add(totalSpecialFV);
        input.setTotalRetirementExpensesFV(fvTotal.setScale(2, RoundingMode.HALF_UP));
        
        return input;
    }

    private void calculateItemFV(Step4ExpenseDTO.ExpenseItem item, int yearsToRetirement, boolean isMonthly) {
        BigDecimal amount = Optional.ofNullable(item.getAmountToday()).orElse(BigDecimal.ZERO);
        BigDecimal annualRate = Optional.ofNullable(item.getInflationRate()).orElse(BigDecimal.ZERO);
        
        BigDecimal rate;
        int periods;

        if (isMonthly) {
            rate = annualRate.divide(BigDecimal.valueOf(100 * 12), 16, RoundingMode.HALF_UP); // Monthly rate
            periods = yearsToRetirement * 12; // Total months
        } else {
            rate = annualRate.divide(BigDecimal.valueOf(100), 16, RoundingMode.HALF_UP); // Annual rate
            periods = yearsToRetirement; // Total years
        }
        
        BigDecimal fv = financialCalculator.calculateFV(amount, rate, periods);
        item.setFutureValue(fv.setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    public List<Step5HavesDTO.CurrentAssetItem> mapSnapshotToCurrentAssets(NetWorthSnapshot snapshot) {
        return snapshot.getItems().stream()
                .filter(item -> item.getType() == NetWorthItem.ItemType.ASSET)
                .map(this::mapToCurrentAssetItem)
                .collect(Collectors.toList());
    }

    private Step5HavesDTO.CurrentAssetItem mapToCurrentAssetItem(NetWorthItem netWorthItem) {
        Step5HavesDTO.CurrentAssetItem assetItem = new Step5HavesDTO.CurrentAssetItem();
        assetItem.setName(netWorthItem.getName());
        assetItem.setPresentValue(netWorthItem.getAmount());
        assetItem.setExpectedReturnRate(getDefaultReturnRate(netWorthItem.getName()));
        return assetItem;
    }

    private BigDecimal getDefaultReturnRate(String name) {
        String lowerCaseName = name.toLowerCase();
        if (lowerCaseName.contains("หุ้น") || lowerCaseName.contains("stock")) return new BigDecimal("10.0");
        if (lowerCaseName.contains("กองทุน") || lowerCaseName.contains("fund")) return new BigDecimal("8.0");
        if (lowerCaseName.contains("rmf") || lowerCaseName.contains("ssf")) return new BigDecimal("5.0");
        if (lowerCaseName.contains("ตราสารหนี้") || lowerCaseName.contains("bond")) return new BigDecimal("3.0");
        if (lowerCaseName.contains("ทอง") || lowerCaseName.contains("gold")) return new BigDecimal("4.0");
        if (lowerCaseName.contains("crypto") || lowerCaseName.contains("bitcoin")) return new BigDecimal("15.0");
        return new BigDecimal("1.0");
    }

    @Override
    public Step5HavesDTO calculateHavesFV(Step5HavesDTO input, int yearsToRetirement) {
        BigDecimal totalCurrentAssetsFV = BigDecimal.ZERO;
        if (input.getCurrentAssets() != null) {
            for (Step5HavesDTO.CurrentAssetItem item : input.getCurrentAssets()) {
                if (item.getPresentValue() != null && item.getExpectedReturnRate() != null) {
                    // For Current Assets, calculate FV based on annual periods and annual rate
                    // Assuming expectedReturnRate is annual
                    BigDecimal annualRate = item.getExpectedReturnRate().divide(BigDecimal.valueOf(100), 16, RoundingMode.HALF_UP);
                    BigDecimal fv = financialCalculator.calculateFV(item.getPresentValue(), annualRate, yearsToRetirement);
                    totalCurrentAssetsFV = totalCurrentAssetsFV.add(fv);
                }
            }
        }
        input.setTotalCurrentAssetsFV(totalCurrentAssetsFV);

        BigDecimal totalFutureAssetsFV = BigDecimal.ZERO;
        if (input.getFutureIncome() != null) {
            totalFutureAssetsFV = totalFutureAssetsFV
                .add(input.getFutureIncome().getGratuity())
                .add(input.getFutureIncome().getSocialSecurityPension())
                .add(input.getFutureIncome().getProvidentFund())
                .add(input.getFutureIncome().getAnnuityInsurance())
                .add(input.getFutureIncome().getLifeInsuranceMaturity())
                .add(input.getFutureIncome().getRealEstateForSale());
        }
        if (input.getFutureAssets() != null) {
            for (Step5HavesDTO.FutureAssetItem item : input.getFutureAssets()) {
                 if (item.getAmount() != null && item.getExpectedReturnRate() != null) {
                    // For Future Assets, calculate FV based on annual periods and annual rate
                    BigDecimal annualRate = item.getExpectedReturnRate().divide(BigDecimal.valueOf(100), 16, RoundingMode.HALF_UP);
                    BigDecimal fv = financialCalculator.calculateFV(item.getAmount(), annualRate, yearsToRetirement);
                    totalFutureAssetsFV = totalFutureAssetsFV.add(fv);
                 }
            }
        }
        input.setTotalFutureAssetsFV(totalFutureAssetsFV);
        
        input.setTotalHavesFV(totalCurrentAssetsFV.add(totalFutureAssetsFV));

        return input;
    }

    @Override
    public DesignResultDTO calculateDesignGap(RetirementPlanData planData) {
        return DesignResultDTO.builder().build();
    }

    @Override
    public List<ScenarioResultDTO> runScenarios(DesignResultDTO baseDesign) {
        return scenarioSimulator.runScenarios(baseDesign);
    }

    @Override
    public Step6DesignDTO calculateDesign(RetirementPlanData planData) {
        BigDecimal totalExpensesFv = planData.getStep4().getTotalRetirementExpensesFV();
        BigDecimal totalHavesFv = planData.getStep5().getTotalHavesFV();
        BigDecimal totalExtraIncome = Optional.ofNullable(planData.getStep3().getTotalExtraIncome()).orElse(BigDecimal.ZERO);
        BigDecimal presentValue = Optional.ofNullable(planData.getStep5().getTotalCurrentAssetsFV()).orElse(BigDecimal.ZERO);
        int yearsToRetirement = Optional.ofNullable(planData.getStep1().getYearsToRetirement()).orElse(0);

        // 1. Calculate Funding Gap
        BigDecimal fundingGap = totalExpensesFv
                .subtract(totalHavesFv)
                .subtract(totalExtraIncome)
                .setScale(2, RoundingMode.HALF_UP);

        // Ensure fundingGap is not negative
        if (fundingGap.compareTo(BigDecimal.ZERO) < 0) {
            fundingGap = BigDecimal.ZERO;
        }

        // 2. Calculate PMT for 3 scenarios
        BigDecimal pmtWorstCase = financialCalculator.calculatePMTWithPV(
                fundingGap, presentValue, new BigDecimal("0.04"), yearsToRetirement, 12
        ).setScale(2, RoundingMode.HALF_UP);

        BigDecimal pmtBaseCase = financialCalculator.calculatePMTWithPV(
                fundingGap, presentValue, new BigDecimal("0.08"), yearsToRetirement, 12
        ).setScale(2, RoundingMode.HALF_UP);

        BigDecimal pmtBestCase = financialCalculator.calculatePMTWithPV(
                fundingGap, presentValue, new BigDecimal("0.15"), yearsToRetirement, 12
        ).setScale(2, RoundingMode.HALF_UP);

        // 3. Generate Chart Data
        List<String> chartLabels = new ArrayList<>();
        List<Step6DesignDTO.ChartDataPoint> worstCaseGrowth = new ArrayList<>();
        List<Step6DesignDTO.ChartDataPoint> baseCaseGrowth = new ArrayList<>();
        List<Step6DesignDTO.ChartDataPoint> bestCaseGrowth = new ArrayList<>();

        // Initial values for year 0
        worstCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(0).amount(presentValue.setScale(2, RoundingMode.HALF_UP)).build());
        baseCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(0).amount(presentValue.setScale(2, RoundingMode.HALF_UP)).build());
        bestCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(0).amount(presentValue.setScale(2, RoundingMode.HALF_UP)).build());
        chartLabels.add("Year " + 0);

        BigDecimal currentWorst = presentValue;
        BigDecimal currentBase = presentValue;
        BigDecimal currentBest = presentValue;

        BigDecimal monthlyRateWorst = new BigDecimal("0.04").divide(BigDecimal.valueOf(12), 16, RoundingMode.HALF_UP);
        BigDecimal monthlyRateBase = new BigDecimal("0.08").divide(BigDecimal.valueOf(12), 16, RoundingMode.HALF_UP);
        BigDecimal monthlyRateBest = new BigDecimal("0.15").divide(BigDecimal.valueOf(12), 16, RoundingMode.HALF_UP);

        for (int year = 1; year <= yearsToRetirement; year++) {
            chartLabels.add("Year " + year);

            // Calculate monthly growth for each scenario
            for (int month = 0; month < 12; month++) {
                currentWorst = currentWorst.multiply(BigDecimal.ONE.add(monthlyRateWorst)).add(pmtWorstCase);
                currentBase = currentBase.multiply(BigDecimal.ONE.add(monthlyRateBase)).add(pmtBaseCase);
                currentBest = currentBest.multiply(BigDecimal.ONE.add(monthlyRateBest)).add(pmtBestCase);
            }
            worstCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(year).amount(currentWorst.setScale(2, RoundingMode.HALF_UP)).build());
            baseCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(year).amount(currentBase.setScale(2, RoundingMode.HALF_UP)).build());
            bestCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(year).amount(currentBest.setScale(2, RoundingMode.HALF_UP)).build());
        }


        return Step6DesignDTO.builder()
                .totalExpensesFv(totalExpensesFv)
                .totalHavesFv(totalHavesFv)
                .totalExtraIncome(totalExtraIncome)
                .presentValue(presentValue)
                .yearsToRetirement(yearsToRetirement)
                .fundingGap(fundingGap)
                .pmtWorstCase(pmtWorstCase)
                .pmtBaseCase(pmtBaseCase)
                .pmtBestCase(pmtBestCase)
                .chartLabels(chartLabels)
                .worstCaseGrowth(worstCaseGrowth)
                .baseCaseGrowth(baseCaseGrowth)
                .bestCaseGrowth(bestCaseGrowth)
                .build();
    }
}
