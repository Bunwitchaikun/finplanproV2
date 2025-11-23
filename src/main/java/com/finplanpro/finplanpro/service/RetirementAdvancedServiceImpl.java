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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetirementAdvancedServiceImpl implements RetirementAdvancedService {

    private final FinancialCalculator financialCalculator;
    private final ScenarioSimulator scenarioSimulator;

    @Override
    public Step1YouDTO calculateStep1(Step1YouDTO input) {
        if (input.getDateOfBirth() == null || input.getRetireYear() == 0) return input;
        LocalDate today = LocalDate.now();
        LocalDate retirementDate = LocalDate.of(input.getRetireYear(), input.getRetireMonth(), 1);
        int currentAge = Period.between(input.getDateOfBirth(), today).getYears();
        int retirementAge = Period.between(input.getDateOfBirth(), retirementDate).getYears();
        input.setCurrentAge(currentAge);
        input.setRetirementAge(retirementAge);
        input.setYearsToRetirement(retirementAge - currentAge);
        return input;
    }

    @Override
    public Step2LifeDTO calculateStep2(Step2LifeDTO input, int retirementAge) {
        // If user provided a specific life expectancy, use it.
        // Otherwise, calculate based on health level.
        if (input.getLifeExpectancy() <= 0) {
            int calculatedLifeExpectancy = switch (input.getHealthLevel()) {
                case "perfect" -> 85;
                case "minor" -> 80;
                case "major" -> 70;
                default -> 75; // moderate or unknown
            };
            input.setLifeExpectancy(calculatedLifeExpectancy);
        }
        
        input.setYearsAfterRetirement(input.getLifeExpectancy() - retirementAge);
        return input;
    }

    @Override
    public Step4ExpenseDTO calculateSpecialExpensesFV(Step4ExpenseDTO input, int yearsToRetirement) {
        if (input.getBasicItems() != null) {
            input.getBasicItems().forEach(item -> calculateItemFV(item, yearsToRetirement));
            BigDecimal totalBasicFV = input.getBasicItems().stream()
                .map(Step4ExpenseDTO.ExpenseItem::getFutureValue).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            input.setTotalBasicExpensesFV(totalBasicFV);
        }

        if (input.getSpecialItems() != null) {
            input.getSpecialItems().forEach(item -> calculateItemFV(item, yearsToRetirement));
            BigDecimal totalSpecialFV = input.getSpecialItems().stream()
                .map(Step4ExpenseDTO.ExpenseItem::getFutureValue).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            input.setTotalSpecialExpensesFV(totalSpecialFV);
        }
        
        return input;
    }

    private void calculateItemFV(Step4ExpenseDTO.ExpenseItem item, int yearsToRetirement) {
        if (item.getAmountToday() != null && item.getInflationRate() != null) {
            BigDecimal fv = financialCalculator.calculateFV(item.getAmountToday(), item.getInflationRate().divide(BigDecimal.valueOf(100)), yearsToRetirement);
            item.setFutureValue(fv.setScale(2, RoundingMode.HALF_UP));
        }
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
        return new BigDecimal("1.0"); // Default for cash or others
    }

    @Override
    public Step5HavesDTO calculateHavesFV(Step5HavesDTO input, int yearsToRetirement) {
        BigDecimal totalCurrentAssetsFV = BigDecimal.ZERO;
        if (input.getCurrentAssets() != null) {
            for (Step5HavesDTO.CurrentAssetItem item : input.getCurrentAssets()) {
                if (item.getPresentValue() != null && item.getExpectedReturnRate() != null) {
                    BigDecimal fv = financialCalculator.calculateFV(item.getPresentValue(), item.getExpectedReturnRate().divide(BigDecimal.valueOf(100)), yearsToRetirement);
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
                    BigDecimal fv = financialCalculator.calculateFV(item.getAmount(), item.getExpectedReturnRate().divide(BigDecimal.valueOf(100)), yearsToRetirement);
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
        // Placeholder for Step 6 logic
        return DesignResultDTO.builder().build();
    }

    @Override
    public List<ScenarioResultDTO> runScenarios(DesignResultDTO baseDesign) {
        return scenarioSimulator.runScenarios(baseDesign);
    }
}
