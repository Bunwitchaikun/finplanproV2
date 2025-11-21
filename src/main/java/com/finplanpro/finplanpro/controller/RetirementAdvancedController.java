package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.dto.*;
import com.finplanpro.finplanpro.service.RetirementAdvancedService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/retirement/advanced")
@SessionAttributes("planData")
public class RetirementAdvancedController {

    private final RetirementAdvancedService retirementService;

    public RetirementAdvancedController(RetirementAdvancedService retirementService) {
        this.retirementService = retirementService;
    }

    @ModelAttribute("planData")
    public RetirementPlanData getPlanData() {
        return new RetirementPlanData();
    }

    private void addActiveMenu(Model model) {
        model.addAttribute("activeMenu", "retirement_advanced");
    }

    @GetMapping("/start")
    public String startNewPlan(SessionStatus status) {
        status.setComplete();
        return "redirect:/retirement/advanced/step1";
    }

    // --- Step 1 to 5 (Existing code) ---
    @GetMapping("/step1")
    public String showStep1(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        model.addAttribute("step1Dto", planData.getStep1());
        addActiveMenu(model);
        return "retirement/advanced/step1";
    }

    @PostMapping("/step1")
    public String processStep1(@ModelAttribute("step1Dto") Step1YouDTO step1Dto, @ModelAttribute("planData") RetirementPlanData planData) {
        Step1YouDTO result = retirementService.calculateStep1(step1Dto);
        planData.setStep1(result);
        return "redirect:/retirement/advanced/step2";
    }

    @GetMapping("/step2")
    public String showStep2(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        model.addAttribute("step2Dto", planData.getStep2());
        addActiveMenu(model);
        return "retirement/advanced/step2";
    }

    @PostMapping("/step2")
    public String processStep2(@ModelAttribute("step2Dto") Step2LifeDTO step2Dto, @ModelAttribute("planData") RetirementPlanData planData) {
        Step2LifeDTO result = retirementService.calculateStep2(step2Dto, planData.getStep1().getRetirementAge());
        planData.setStep2(result);
        return "redirect:/retirement/advanced/step3";
    }

    @GetMapping("/step3")
    public String showStep3(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        model.addAttribute("step3Dto", planData.getStep3());
        addActiveMenu(model);
        return "retirement/advanced/step3";
    }

    @PostMapping("/step3")
    public String processStep3(@ModelAttribute("step3Dto") Step3WantsDTO step3Dto, @ModelAttribute("planData") RetirementPlanData planData) {
        planData.setStep3(step3Dto);
        return "redirect:/retirement/advanced/step4";
    }

    @GetMapping("/step4")
    public String showStep4(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        model.addAttribute("step4Dto", planData.getStep4());
        addActiveMenu(model);
        return "retirement/advanced/step4";
    }

    @PostMapping("/step4")
    public String processStep4(@ModelAttribute("step4Dto") Step4ExpenseDTO step4Dto, @ModelAttribute("planData") RetirementPlanData planData) {
        Step4ExpenseDTO result = retirementService.calculateSpecialExpensesFV(step4Dto, planData.getStep1().getYearsToRetirement());
        planData.setStep4(result);
        return "redirect:/retirement/advanced/step5";
    }

    @GetMapping("/step5")
    public String showStep5(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        model.addAttribute("step5Dto", planData.getStep5());
        addActiveMenu(model);
        return "retirement/advanced/step5";
    }

    @PostMapping("/step5")
    public String processStep5(@ModelAttribute("step5Dto") Step5HavesDTO step5Dto, @ModelAttribute("planData") RetirementPlanData planData) {
        Step5HavesDTO result = retirementService.calculateAssetsFV(step5Dto, planData.getStep1().getYearsToRetirement());
        planData.setStep5(result);
        return "redirect:/retirement/advanced/step6";
    }

    // --- Step 6: DESIGN ---
    @GetMapping("/step6")
    public String showStep6(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        BigDecimal returnBefore = new BigDecimal("0.08");
        BigDecimal returnAfter = new BigDecimal("0.05");
        
        DesignResultDTO result = retirementService.calculateDesignGap(
                planData.getStep3().getAfterTaxIncome(),
                new BigDecimal("0.03"),
                planData.getStep1().getYearsToRetirement(),
                planData.getStep2().getYearsAfterRetirement(),
                returnBefore,
                returnAfter,
                planData.getStep5().getTotalAssetsFV(),
                planData.getStep4().getTotalSpecialExpensesFV()
        );
        planData.setDesignResult(result);
        model.addAttribute("designResult", result);
        addActiveMenu(model);
        return "retirement/advanced/step6";
    }

    @PostMapping("/step6")
    public String processStep6() {
        return "redirect:/retirement/advanced/step7";
    }

    // --- Step 7: TEST & SAVE ---
    @GetMapping("/step7")
    public String showStep7(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        List<ScenarioResultDTO> scenarios = retirementService.runScenarios(planData.getDesignResult());
        planData.setScenarios(scenarios);
        model.addAttribute("scenarios", scenarios);
        addActiveMenu(model);
        return "retirement/advanced/step7";
    }

    @PostMapping("/save")
    public String savePlan(SessionStatus status) {
        // Here you would call a service to save the 'planData' object to the database
        status.setComplete(); // Clear session attribute after saving
        return "redirect:/dashboard";
    }
}
