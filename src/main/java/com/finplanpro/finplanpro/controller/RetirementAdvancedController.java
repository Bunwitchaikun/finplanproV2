package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.dto.*;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.entity.RetirementAdvanced;
import com.finplanpro.finplanpro.service.NetWorthSnapshotService;
import com.finplanpro.finplanpro.service.RetirementAdvancedService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/retirement/advanced")
@SessionAttributes("planData")
public class RetirementAdvancedController {

    private final RetirementAdvancedService retirementService;
    private final NetWorthSnapshotService netWorthSnapshotService;

    public RetirementAdvancedController(RetirementAdvancedService retirementService,
            NetWorthSnapshotService netWorthSnapshotService) {
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
    public String showStep1(Model model, @ModelAttribute("planData") RetirementPlanData planData,
            Authentication authentication) {
        model.addAttribute("step1Dto", planData.getStep1());
        // Load saved plans list
        if (authentication != null) {
            List<RetirementAdvanced> savedPlans = retirementService.findAllPlansByUser(authentication.getName());
            model.addAttribute("savedPlans", savedPlans);
        }
        addActiveMenu(model);
        return "retirement/advanced/step1";
    }

    @PostMapping("/step1")
    public String processStep1(@ModelAttribute("step1Dto") Step1YouDTO step1Dto,
            @ModelAttribute("planData") RetirementPlanData planData) {
        // Calculate and save to session
        Step1YouDTO calculatedStep1 = retirementService.calculateStep1(step1Dto);
        planData.getStep1().setCurrentAge(calculatedStep1.getCurrentAge());
        planData.getStep1().setGender(calculatedStep1.getGender());
        planData.getStep1().setRetirementAge(calculatedStep1.getRetirementAge());
        planData.getStep1().setYearsToRetirement(calculatedStep1.getYearsToRetirement());
        planData.getStep1().setPlanName(step1Dto.getPlanName());
        planData.setPlanName(step1Dto.getPlanName()); // Save to session
        return "redirect:/retirement/advanced/step2";
    }

    @GetMapping("/step2")
    public String showStep2(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        model.addAttribute("step2Dto", planData.getStep2());
        addActiveMenu(model);
        return "retirement/advanced/step2";
    }

    @PostMapping("/step2")
    public String processStep2(@ModelAttribute("step2Dto") Step2LifeDTO step2Dto,
            @ModelAttribute("planData") RetirementPlanData planData) {
        String gender = planData.getStep1().getGender();
        Integer retirementAge = planData.getStep1().getRetirementAge();
        planData.setStep2(
                retirementService.calculateStep2(step2Dto, retirementAge != null ? retirementAge : 0, gender));
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
    public String processStep3(@ModelAttribute("step3Dto") Step3WantsDTO step3Dto,
            @ModelAttribute("planData") RetirementPlanData planData) {
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
        planData.setStep4(retirementService.calculateSpecialExpensesFV(step4Dto,
                yearsToRetirement != null ? yearsToRetirement : 0,
                yearsAfterRetirement != null ? yearsAfterRetirement : 0));

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
    public String processStep5(@ModelAttribute("step5Dto") Step5HavesDTO step5Dto,
            @ModelAttribute("planData") RetirementPlanData planData) {
        Integer yearsToRetirement = planData.getStep1().getYearsToRetirement();
        planData.setStep5(
                retirementService.calculateHavesFV(step5Dto, yearsToRetirement != null ? yearsToRetirement : 0));
        return "redirect:/retirement/advanced/step6";
    }

    @GetMapping("/step6")
    public String showStep6(Model model, @ModelAttribute("planData") RetirementPlanData planData) {
        Step6DesignDTO step6Dto = retirementService.calculateDesign(planData);
        model.addAttribute("step6Dto", step6Dto);
        addActiveMenu(model);
        return "retirement/advanced/step6";
    }

    @PostMapping("/save")
    public String savePlan(@ModelAttribute("planData") RetirementPlanData planData,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        if (authentication != null) {
            retirementService.savePlan(planData, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage",
                    "แผนการเกษียณ '" + planData.getPlanName() + "' ถูกบันทึกเรียบร้อยแล้ว!");
        }
        return "redirect:/retirement/advanced/start";
    }

    @GetMapping("/list")
    public String showList(Model model, Authentication authentication) {
        if (authentication != null) {
            List<RetirementAdvanced> plans = retirementService.findAllPlansByUser(authentication.getName());
            model.addAttribute("plans", plans);
        }
        addActiveMenu(model);
        return "retirement/advanced/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id,
            @ModelAttribute("planData") RetirementPlanData planData,
            RedirectAttributes redirectAttributes) {
        try {
            RetirementPlanData loadedPlan = retirementService.loadPlanToSession(id);
            // Copy loaded data to session
            planData.setId(loadedPlan.getId()); // Set ID to session
            planData.setPlanName(loadedPlan.getPlanName());
            planData.setStep1(loadedPlan.getStep1());
            planData.setStep2(loadedPlan.getStep2());
            planData.setStep3(loadedPlan.getStep3());
            planData.setStep4(loadedPlan.getStep4());
            planData.setStep5(loadedPlan.getStep5());
            return "redirect:/retirement/advanced/step1";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "ไม่พบแผนที่ต้องการแก้ไข");
            return "redirect:/retirement/advanced/list";
        }
    }

    @PostMapping("/delete-selected")
    public String deleteSelected(@RequestParam(value = "selectedIds", required = false) List<UUID> selectedIds,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        if (selectedIds == null || selectedIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "กรุณาเลือกอย่างน้อยหนึ่งแผนเพื่อลบ");
            return "redirect:/retirement/advanced/list";
        }
        try {
            if (authentication != null) {
                retirementService.deletePlansByIds(selectedIds, authentication.getName());
                redirectAttributes.addFlashAttribute("successMessage", "ลบแผนที่เลือกเรียบร้อยแล้ว");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "เกิดข้อผิดพลาดในการลบแผน");
        }
        return "redirect:/retirement/advanced/list";
    }

    @PostMapping("/delete/all")
    public String deleteAll(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            if (authentication != null) {
                retirementService.deleteAllPlansByUser(authentication.getName());
                redirectAttributes.addFlashAttribute("successMessage", "ลบแผนทั้งหมดเรียบร้อยแล้ว");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "เกิดข้อผิดพลาดในการลบแผนทั้งหมด");
        }
        return "redirect:/retirement/advanced/list";
    }
}
