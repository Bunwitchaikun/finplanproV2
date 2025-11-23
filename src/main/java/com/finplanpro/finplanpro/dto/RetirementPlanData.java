package com.finplanpro.finplanpro.dto;

import lombok.Data;

/**
 * คลาสสำหรับเก็บข้อมูลแผนเกษียณทั้งหมดไว้ใน Session ระหว่างทำ Wizard
 */
@Data
public class RetirementPlanData {
    private Step1YouDTO step1 = new Step1YouDTO();
    private Step2LifeDTO step2 = new Step2LifeDTO();
    private Step3WantsDTO step3 = new Step3WantsDTO();
    private Step4ExpenseDTO step4 = new Step4ExpenseDTO();
    private Step5HavesDTO step5 = new Step5HavesDTO();
    private AssetLiabilityDTO step5AssetsLiabilities = new AssetLiabilityDTO();
    private DesignResultDTO designResult;
    private java.util.List<ScenarioResultDTO> scenarios;
}
