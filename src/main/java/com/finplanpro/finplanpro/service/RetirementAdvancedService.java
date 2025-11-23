package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.*;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;

import java.math.BigDecimal;
import java.util.List;

public interface RetirementAdvancedService {
    Step1YouDTO calculateStep1(Step1YouDTO input);
    Step2LifeDTO calculateStep2(Step2LifeDTO input, int retirementAge, String gender);
    Step4ExpenseDTO calculateSpecialExpensesFV(Step4ExpenseDTO input, int yearsToRetirement);
    
    List<Step5HavesDTO.CurrentAssetItem> mapSnapshotToCurrentAssets(NetWorthSnapshot snapshot);
    Step5HavesDTO calculateHavesFV(Step5HavesDTO input, int yearsToRetirement);

    DesignResultDTO calculateDesignGap(RetirementPlanData planData);
    List<ScenarioResultDTO> runScenarios(DesignResultDTO baseDesign);
}
