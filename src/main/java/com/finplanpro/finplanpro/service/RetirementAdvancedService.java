package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.*;
import java.math.BigDecimal;
import java.util.List;

public interface RetirementAdvancedService {
    Step1YouDTO calculateStep1(Step1YouDTO input);
    Step2LifeDTO calculateStep2(Step2LifeDTO input, int retirementAge);
    Step3WantsDTO calculateStep3IncomeProjection(Step3WantsDTO input);
    Step4ExpenseDTO calculateSpecialExpensesFV(Step4ExpenseDTO input, int yearsToRetirement);
    Step5HavesDTO calculateAssetsFV(Step5HavesDTO input, int yearsToRetirement);
    DesignResultDTO calculateDesignGap(BigDecimal monthlyCostToday, BigDecimal inflation, int yearsToRetirement, int yearsAfterRetirement, BigDecimal returnBeforeRetirement, BigDecimal returnAfterRetirement, BigDecimal totalAssetsFV, BigDecimal totalSpecialExpensesFV);
    List<ScenarioResultDTO> runScenarios(DesignResultDTO baseDesign);
}
