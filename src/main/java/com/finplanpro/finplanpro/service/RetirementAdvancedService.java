package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.*;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RetirementAdvancedService {
    Step1YouDTO calculateStep1(Step1YouDTO input);

    Step2LifeDTO calculateStep2(Step2LifeDTO input, int retirementAge, String gender);

    Step4ExpenseDTO calculateSpecialExpensesFV(Step4ExpenseDTO input, int yearsToRetirement, int yearsAfterRetirement);

    List<Step5HavesDTO.CurrentAssetItem> mapSnapshotToCurrentAssets(NetWorthSnapshot snapshot);

    Step5HavesDTO calculateHavesFV(Step5HavesDTO input, int yearsToRetirement);

    DesignResultDTO calculateDesignGap(RetirementPlanData planData);

    List<ScenarioResultDTO> runScenarios(DesignResultDTO baseDesign);

    Step6DesignDTO calculateDesign(RetirementPlanData planData);

    // New methods for save/load functionality
    void savePlan(RetirementPlanData planData, String username);

    List<com.finplanpro.finplanpro.entity.RetirementAdvanced> findAllPlansByUser(String username);

    com.finplanpro.finplanpro.entity.RetirementAdvanced findPlanById(UUID id);

    RetirementPlanData loadPlanToSession(UUID id);

    void deletePlansByIds(List<UUID> ids, String username);

    void deleteAllPlansByUser(String username);
}
