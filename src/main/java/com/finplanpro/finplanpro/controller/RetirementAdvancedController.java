package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.dto.*;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.service.NetWorthSnapshotService;
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
    private final NetWorthSnapshotService netWorthSnapshotService;

    public RetirementAdvancedController(RetirementAdvancedService retirementService, NetWorthSnapshotService netWorthSnapshotService) {
        this.retirementService = retirementService;
        this.netWorthSnapshotService = netWorthSnapshotService;
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

    // --- Steps 1 to 4 (Existing code) ---
    @GetMapping("/step1")
    public String showStep1(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        model.addAttribute("step1Dto", planData.getStep1());
        addActiveMenu(model);
        return "retirement/advanced/step1";
    }

    @PostMapping("/step1")
    public String processStep1(@ModelAttribute("step1Dto") Step1YouDTO step1Dto, @ModelAttribute("planData") RetirementPlanData planData) {
        planData.setStep1(retirementService.calculateStep1(step1Dto));
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
        planData.setStep2(retirementService.calculateStep2(step2Dto, planData.getStep1().getRetirementAge()));
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
        planData.setStep4(retirementService.calculateSpecialExpensesFV(step4Dto, planData.getStep1().getYearsToRetirement()));
        return "redirect:/retirement/advanced/step5";
    }

    // --- Step 5: HAVES (UPDATED) ---
    @GetMapping("/step5")
    public String showStep5(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        if (planData.getStep5().getCurrentAssets().isEmpty()) {
            List<NetWorthSnapshot> snapshots = netWorthSnapshotService.findSnapshotsByUser();
            if (!snapshots.isEmpty()) {
                planData.getStep5().setCurrentAssets(retirementService.mapSnapshotToCurrentAssets(snapshots.get(0)));
            }
        }
        
        model.addAttribute("step5Dto", planData.getStep5());
        // Pass total expenses from Step 4 to the view
        model.addAttribute("totalExpensesFV", planData.getStep4().getTotalRetirementExpensesFV());
        addActiveMenu(model);
        return "retirement/advanced/step5";
    }

    @PostMapping("/step5")
    public String processStep5(@ModelAttribute("step5Dto") Step5HavesDTO step5Dto, @ModelAttribute("planData") RetirementPlanData planData) {
        planData.setStep5(retirementService.calculateHavesFV(step5Dto, planData.getStep1().getYearsToRetirement()));
        return "redirect:/retirement/advanced/step6";
    }

    // --- Steps 6 & 7 (Placeholders) ---
    @GetMapping("/step6") public String showStep6(Model model, @ModelAttribute("planData") RetirementPlanData planData) { addActiveMenu(model); return "retirement/advanced/step6"; }
    @PostMapping("/step6") public String processStep6() { return "redirect:/retirement/advanced/step7"; }
    @GetMapping("/step7") public String showStep7(Model model) { addActiveMenu(model); return "retirement/advanced/step7"; }
    @PostMapping("/save") public String savePlan() { return "redirect:/dashboard"; }
}
