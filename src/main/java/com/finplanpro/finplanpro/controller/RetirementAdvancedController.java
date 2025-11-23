package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.dto.*;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.service.NetWorthSnapshotService;
import com.finplanpro.finplanpro.service.RetirementAdvancedService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

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

    @GetMapping("/step1")
    public String showStep1(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        model.addAttribute("step1Dto", planData.getStep1());
        addActiveMenu(model);
        return "retirement/advanced/step1";
    }

    @PostMapping("/step1")
    public String processStep1(@ModelAttribute("step1Dto") Step1YouDTO step1Dto, @ModelAttribute("planData") RetirementPlanData planData) {
        // Calculate and save to session
        Step1YouDTO calculatedStep1 = retirementService.calculateStep1(step1Dto);
        planData.getStep1().setCurrentAge(calculatedStep1.getCurrentAge());
        planData.getStep1().setGender(calculatedStep1.getGender());
        planData.getStep1().setRetirementAge(calculatedStep1.getRetirementAge());
        planData.getStep1().setYearsToRetirement(calculatedStep1.getYearsToRetirement());
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
        String gender = planData.getStep1().getGender();
        Integer retirementAge = planData.getStep1().getRetirementAge();
        planData.setStep2(retirementService.calculateStep2(step2Dto, retirementAge != null ? retirementAge : 0, gender));
        return "redirect:/retirement/advanced/step3";
    }

    @GetMapping("/step3")
    public String showStep3(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        Step3WantsDTO step3Dto = planData.getStep3();
        Integer retirementAge = planData.getStep1().getRetirementAge();
        int years = planData.getStep2().getLifeExpectancy() - (retirementAge != null ? retirementAge : 0);
        step3Dto.setExtraIncomeYears(Math.max(0, years));
        model.addAttribute("step3Dto", step3Dto);
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
        model.addAttribute("yearsAfterRetirement", planData.getStep2().getYearsAfterRetirement());
        addActiveMenu(model);
        return "retirement/advanced/step4";
    }

    @PostMapping("/step4")
    public String processStep4(@ModelAttribute("step4Dto") Step4ExpenseDTO step4Dto, 
                               @ModelAttribute("planData") RetirementPlanData planData,
                               @RequestParam("action") String action,
                               Model model) {
        Integer yearsToRetirement = planData.getStep1().getYearsToRetirement();
        Integer yearsAfterRetirement = planData.getStep2().getYearsAfterRetirement();
        planData.setStep4(retirementService.calculateSpecialExpensesFV(step4Dto, yearsToRetirement != null ? yearsToRetirement : 0, yearsAfterRetirement != null ? yearsAfterRetirement : 0));

        if ("calculate".equals(action)) {
            addActiveMenu(model);
            model.addAttribute("step4Dto", planData.getStep4());
            model.addAttribute("yearsAfterRetirement", planData.getStep2().getYearsAfterRetirement());
            return "retirement/advanced/step4";
        }
        
        return "redirect:/retirement/advanced/step5";
    }

    @GetMapping("/step5")
    public String showStep5(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        if (planData.getStep5().getCurrentAssets().isEmpty()) {
            List<NetWorthSnapshot> snapshots = netWorthSnapshotService.findSnapshotsByUser();
            if (!snapshots.isEmpty()) {
                planData.getStep5().setCurrentAssets(retirementService.mapSnapshotToCurrentAssets(snapshots.get(0)));
            }
        }
        model.addAttribute("step5Dto", planData.getStep5());
        model.addAttribute("totalExpensesFV", planData.getStep4().getTotalRetirementExpensesFV());
        addActiveMenu(model);
        return "retirement/advanced/step5";
    }

    @PostMapping("/step5")
    public String processStep5(@ModelAttribute("step5Dto") Step5HavesDTO step5Dto, @ModelAttribute("planData") RetirementPlanData planData) {
        Integer yearsToRetirement = planData.getStep1().getYearsToRetirement();
        planData.setStep5(retirementService.calculateHavesFV(step5Dto, yearsToRetirement != null ? yearsToRetirement : 0));
        return "redirect:/retirement/advanced/step6";
    }

    @GetMapping("/step6") public String showStep6(Model model) { addActiveMenu(model); return "retirement/advanced/step6"; }
    @PostMapping("/step6") public String processStep6() { return "redirect:/retirement/advanced/step7"; }
    @GetMapping("/step7") public String showStep7(Model model) { addActiveMenu(model); return "retirement/advanced/step7"; }
    @PostMapping("/save") public String savePlan() { return "redirect:/dashboard"; }
}
