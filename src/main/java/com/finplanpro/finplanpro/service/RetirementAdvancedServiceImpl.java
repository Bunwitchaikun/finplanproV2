package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.*;
import com.finplanpro.finplanpro.entity.NetWorthItem;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.entity.RetirementAdvanced;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.RetirementAdvancedRepository;
import com.finplanpro.finplanpro.service.calculation.FinancialCalculator;
import com.finplanpro.finplanpro.service.calculation.ScenarioSimulator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetirementAdvancedServiceImpl implements RetirementAdvancedService {

    private final FinancialCalculator financialCalculator;
    private final ScenarioSimulator scenarioSimulator;
    private final RetirementAdvancedRepository retirementAdvancedRepository;
    private final UserService userService;

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
    public Step4ExpenseDTO calculateSpecialExpensesFV(Step4ExpenseDTO input, int yearsToRetirement,
            int yearsAfterRetirement) {
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
        // It should be totalAnnualizedBasicFV (sum of annual FV at retirement) *
        // yearsAfterRetirement
        BigDecimal totalUntilEndOfLife = totalAnnualizedBasicFV.multiply(BigDecimal.valueOf(yearsAfterRetirement));
        input.setTotalBasicExpensesUntilEndOfLife(totalUntilEndOfLife.setScale(2, RoundingMode.HALF_UP));

        // --- Special Items Calculation ---
        BigDecimal totalSpecialToday = BigDecimal.ZERO;
        BigDecimal totalSpecialFV = BigDecimal.ZERO;
        if (input.getSpecialItems() != null) {
            for (Step4ExpenseDTO.ExpenseItem item : input.getSpecialItems()) {
                // For Special Items, calculate FV based on annual periods and annual rate
                calculateItemFV(item, yearsToRetirement, false); // false for annual
                totalSpecialToday = totalSpecialToday
                        .add(Optional.ofNullable(item.getAmountToday()).orElse(BigDecimal.ZERO));
                totalSpecialFV = totalSpecialFV.add(item.getFutureValue());
            }
        }
        input.setTotalSpecialExpensesToday(totalSpecialToday);
        input.setTotalSpecialExpensesFV(totalSpecialFV);

        // --- Grand Totals (New Formulas) ---
        // totalAnnualizedBasicToday is sum of (monthly_today * 12)
        BigDecimal todayTotal = (totalAnnualizedBasicToday.multiply(BigDecimal.valueOf(yearsAfterRetirement)))
                .add(totalSpecialToday);
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
        if (lowerCaseName.contains("หุ้น") || lowerCaseName.contains("stock"))
            return new BigDecimal("10.0");
        if (lowerCaseName.contains("กองทุน") || lowerCaseName.contains("fund"))
            return new BigDecimal("8.0");
        if (lowerCaseName.contains("rmf") || lowerCaseName.contains("ssf"))
            return new BigDecimal("5.0");
        if (lowerCaseName.contains("ตราสารหนี้") || lowerCaseName.contains("bond"))
            return new BigDecimal("3.0");
        if (lowerCaseName.contains("ทอง") || lowerCaseName.contains("gold"))
            return new BigDecimal("4.0");
        if (lowerCaseName.contains("crypto") || lowerCaseName.contains("bitcoin"))
            return new BigDecimal("15.0");
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
                    BigDecimal annualRate = item.getExpectedReturnRate().divide(BigDecimal.valueOf(100), 16,
                            RoundingMode.HALF_UP);
                    BigDecimal fv = financialCalculator.calculateFV(item.getPresentValue(), annualRate,
                            yearsToRetirement);
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
                    BigDecimal annualRate = item.getExpectedReturnRate().divide(BigDecimal.valueOf(100), 16,
                            RoundingMode.HALF_UP);
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
        BigDecimal totalExtraIncome = Optional.ofNullable(planData.getStep3().getTotalExtraIncome())
                .orElse(BigDecimal.ZERO);
        BigDecimal presentValueForChart = Optional.ofNullable(planData.getStep5().getTotalCurrentAssetsFV())
                .orElse(BigDecimal.ZERO);
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

        // Define monthly rates and total periods
        int totalPeriods = yearsToRetirement * 12;
        BigDecimal monthlyRateWorst = new BigDecimal("0.04").divide(BigDecimal.valueOf(12), FinancialCalculator.SCALE,
                RoundingMode.HALF_UP);
        BigDecimal monthlyRateBase = new BigDecimal("0.08").divide(BigDecimal.valueOf(12), FinancialCalculator.SCALE,
                RoundingMode.HALF_UP);
        BigDecimal monthlyRateBest = new BigDecimal("0.15").divide(BigDecimal.valueOf(12), FinancialCalculator.SCALE,
                RoundingMode.HALF_UP);

        // 2. Calculate PMT for 3 scenarios using calculatePMT (no initial PV)
        // We negate fundingGap to get a positive PMT result, as some financial
        // functions return negative for outflows.
        BigDecimal pmtWorstCase = financialCalculator.calculatePMT(
                fundingGap.negate(), monthlyRateWorst, totalPeriods).abs().setScale(2, RoundingMode.HALF_UP);

        BigDecimal pmtBaseCase = financialCalculator.calculatePMT(
                fundingGap.negate(), monthlyRateBase, totalPeriods).abs().setScale(2, RoundingMode.HALF_UP);

        BigDecimal pmtBestCase = financialCalculator.calculatePMT(
                fundingGap.negate(), monthlyRateBest, totalPeriods).abs().setScale(2, RoundingMode.HALF_UP);

        // 3. Generate Chart Data
        List<String> chartLabels = new ArrayList<>();
        List<Step6DesignDTO.ChartDataPoint> worstCaseGrowth = new ArrayList<>();
        List<Step6DesignDTO.ChartDataPoint> baseCaseGrowth = new ArrayList<>();
        List<Step6DesignDTO.ChartDataPoint> bestCaseGrowth = new ArrayList<>();

        // The chart should show the growth of the *new contributions* towards the
        // *fundingGap*.
        // So, it starts from zero.
        BigDecimal initialChartValue = BigDecimal.ZERO;

        worstCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(0).amount(initialChartValue).build());
        baseCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(0).amount(initialChartValue).build());
        bestCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(0).amount(initialChartValue).build());
        chartLabels.add("Year " + 0);

        BigDecimal currentWorst = initialChartValue;
        BigDecimal currentBase = initialChartValue;
        BigDecimal currentBest = initialChartValue;

        for (int year = 1; year <= yearsToRetirement; year++) {
            chartLabels.add("Year " + year);

            // Calculate monthly growth for each scenario
            for (int month = 0; month < 12; month++) {
                currentWorst = currentWorst.multiply(BigDecimal.ONE.add(monthlyRateWorst)).add(pmtWorstCase);
                currentBase = currentBase.multiply(BigDecimal.ONE.add(monthlyRateBase)).add(pmtBaseCase);
                currentBest = currentBest.multiply(BigDecimal.ONE.add(monthlyRateBest)).add(pmtBestCase);
            }
            worstCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(year)
                    .amount(currentWorst.setScale(2, RoundingMode.HALF_UP)).build());
            baseCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(year)
                    .amount(currentBase.setScale(2, RoundingMode.HALF_UP)).build());
            bestCaseGrowth.add(Step6DesignDTO.ChartDataPoint.builder().year(year)
                    .amount(currentBest.setScale(2, RoundingMode.HALF_UP)).build());
        }

        return Step6DesignDTO.builder()
                .totalExpensesFv(totalExpensesFv)
                .totalHavesFv(totalHavesFv)
                .totalExtraIncome(totalExtraIncome)
                .presentValue(presentValueForChart) // Keep this for display if needed, but not for PMT calc
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

    @Override
    @Transactional
    public void savePlan(RetirementPlanData planData, String username) {
        // Try email first (since authentication.getName() returns email), then username
        User user = userService.findUserByEmail(username);
        if (user == null) {
            user = userService.findUserByUsername(username);
        }
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }

        RetirementAdvanced entity = new RetirementAdvanced();
        entity.setUser(user);
        entity.setPlanName(planData.getPlanName());

        // Map Step1 data
        if (planData.getStep1() != null) {
            Step1YouDTO step1 = planData.getStep1();
            if (step1.getCurrentAge() != null) {
                // Calculate date of birth from current age
                LocalDate dob = LocalDate.now().minusYears(step1.getCurrentAge());
                entity.setDateOfBirth(dob);
            }
            entity.setGender(step1.getGender());
            if (step1.getRetirementAge() != null) {
                entity.setRetireAge(step1.getRetirementAge());
            }
        }

        // Map Step2 data
        if (planData.getStep2() != null && planData.getStep2().getLifeExpectancy() != 0) {
            entity.setLifeExpectancy(planData.getStep2().getLifeExpectancy());
        }

        // Map Step3 data
        if (planData.getStep3() != null) {
            entity.setLifestyle(planData.getStep3().getLifestyleChoice());
        }

        // Map Step4 data
        if (planData.getStep4() != null) {
            Step4ExpenseDTO step4 = planData.getStep4();
            entity.setDesiredMonthlyExpense(
                    step4.getTotalBasicExpensesFV() != null ? step4.getTotalBasicExpensesFV() : BigDecimal.ZERO);
            entity.setSpecialExpense(
                    step4.getTotalSpecialExpensesFV() != null ? step4.getTotalSpecialExpensesFV() : BigDecimal.ZERO);
        }

        // Map Step5 data
        if (planData.getStep5() != null) {
            Step5HavesDTO step5 = planData.getStep5();
            entity.setCurrentAssets(
                    step5.getTotalCurrentAssetsFV() != null ? step5.getTotalCurrentAssetsFV() : BigDecimal.ZERO);

            if (step5.getFutureIncome() != null) {
                Step5HavesDTO.FutureIncome futureIncome = step5.getFutureIncome();
                entity.setRmfSsf(BigDecimal.ZERO); // Can be enhanced to store specific values
                entity.setPension(
                        futureIncome.getSocialSecurityPension() != null ? futureIncome.getSocialSecurityPension()
                                : BigDecimal.ZERO);
                entity.setAnnuity(futureIncome.getAnnuityInsurance() != null ? futureIncome.getAnnuityInsurance()
                        : BigDecimal.ZERO);
            }
        }

        // Calculate and save Step6 results
        Step6DesignDTO step6 = calculateDesign(planData);
        entity.setTotalFundsNeeded(step6.getTotalExpensesFv());
        entity.setFundGap(step6.getFundingGap());

        retirementAdvancedRepository.save(entity);
    }

    @Override
    public List<RetirementAdvanced> findAllPlansByUser(String username) {
        // Try email first (since authentication.getName() returns email), then username
        User user = userService.findUserByEmail(username);
        if (user == null) {
            user = userService.findUserByUsername(username);
        }
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        return retirementAdvancedRepository.findByUserOrderByIdDesc(user);
    }

    @Override
    public RetirementAdvanced findPlanById(UUID id) {
        return retirementAdvancedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Retirement plan not found with id: " + id));
    }

    @Override
    public RetirementPlanData loadPlanToSession(UUID id) {
        RetirementAdvanced entity = findPlanById(id);
        RetirementPlanData planData = new RetirementPlanData();

        // Load plan name
        planData.setPlanName(entity.getPlanName());

        // Load Step1 data
        Step1YouDTO step1 = new Step1YouDTO();
        if (entity.getDateOfBirth() != null) {
            int currentAge = Period.between(entity.getDateOfBirth(), LocalDate.now()).getYears();
            step1.setCurrentAge(currentAge);
        }
        step1.setGender(entity.getGender());
        step1.setRetirementAge(entity.getRetireAge());
        step1.setPlanName(entity.getPlanName());
        if (step1.getCurrentAge() != null && step1.getRetirementAge() != null) {
            step1.setYearsToRetirement(step1.getRetirementAge() - step1.getCurrentAge());
        }
        planData.setStep1(step1);

        // Load Step2 data
        Step2LifeDTO step2 = new Step2LifeDTO();
        if (entity.getLifeExpectancy() != null) {
            step2.setLifeExpectancy(entity.getLifeExpectancy());
        }
        if (entity.getRetireAge() != null && entity.getLifeExpectancy() != null) {
            step2.setYearsAfterRetirement(entity.getLifeExpectancy() - entity.getRetireAge());
        }
        planData.setStep2(step2);

        // Load Step3 data
        Step3WantsDTO step3 = new Step3WantsDTO();
        if (entity.getLifestyle() != null) {
            step3.setLifestyleChoice(entity.getLifestyle());
        }
        planData.setStep3(step3);

        // Load Step4 data - Note: We can only load summary values, not individual items
        Step4ExpenseDTO step4 = new Step4ExpenseDTO();
        step4.setTotalBasicExpensesFV(entity.getDesiredMonthlyExpense());
        step4.setTotalSpecialExpensesFV(entity.getSpecialExpense());
        planData.setStep4(step4);

        // Load Step5 data
        Step5HavesDTO step5 = new Step5HavesDTO();
        step5.setTotalCurrentAssetsFV(entity.getCurrentAssets());

        Step5HavesDTO.FutureIncome futureIncome = new Step5HavesDTO.FutureIncome();
        futureIncome.setSocialSecurityPension(entity.getPension());
        futureIncome.setAnnuityInsurance(entity.getAnnuity());
        step5.setFutureIncome(futureIncome);
        planData.setStep5(step5);

        return planData;
    }

    @Override
    @Transactional
    public void deletePlansByIds(List<UUID> ids, String username) {
        // Try email first (since authentication.getName() returns email), then username
        User user = userService.findUserByEmail(username);
        if (user == null) {
            user = userService.findUserByUsername(username);
        }
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        retirementAdvancedRepository.deleteByIdInAndUser(ids, user);
    }

    @Override
    @Transactional
    public void deleteAllPlansByUser(String username) {
        // Try email first (since authentication.getName() returns email), then username
        User user = userService.findUserByEmail(username);
        if (user == null) {
            user = userService.findUserByUsername(username);
        }
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }
        retirementAdvancedRepository.deleteByUser(user);
    }
}
