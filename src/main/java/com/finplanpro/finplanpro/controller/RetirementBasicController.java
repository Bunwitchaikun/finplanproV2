package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.dto.RetirementBasicResult;
import com.finplanpro.finplanpro.entity.RetirementBasic;
import com.finplanpro.finplanpro.service.RetirementBasicService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

@Controller
@RequestMapping("/retirement/basic")
public class RetirementBasicController {

    private final RetirementBasicService retirementBasicService;

    public RetirementBasicController(RetirementBasicService retirementBasicService) {
        this.retirementBasicService = retirementBasicService;
    }

    @GetMapping
    public String showRetirementPage(Model model) {
        if (!model.containsAttribute("newPlan")) {
            model.addAttribute("newPlan", defaultPlan());
        }
        List<RetirementBasic> userPlans = retirementBasicService.findPlansByUser();
        model.addAttribute("userPlans", userPlans);
        model.addAttribute("activeMenu", "retirement");
        return "retirement/basic/retirement_basic_form";
    }

    @PostMapping
    public String calculateAndSavePlan(@Valid @ModelAttribute("newPlan") RetirementBasic newPlan,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newPlan", result);
            redirectAttributes.addFlashAttribute("newPlan", newPlan);
            return "redirect:/retirement/basic";
        }

        try {
            RetirementBasic savedPlan = retirementBasicService.calculateAndSave(newPlan);
            redirectAttributes.addFlashAttribute("calcResult", buildResult(savedPlan));
            redirectAttributes.addFlashAttribute("successMessage",
                    "บันทึกแผน \"" + savedPlan.getPlanName() + "\" สำเร็จ ต้องเตรียมเงิน " + formatBaht(savedPlan.getTotalFundsNeeded()));
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            redirectAttributes.addFlashAttribute("newPlan", newPlan);
        }

        return "redirect:/retirement/basic";
    }

    @PostMapping("/delete/{id}")
    public String deletePlan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            retirementBasicService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Plan deleted successfully.");
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: You are not authorized to delete this plan.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting plan.");
        }
        return "redirect:/retirement/basic";
    }

    private RetirementBasic defaultPlan() {
        RetirementBasic plan = new RetirementBasic();
        plan.setPlanName("แผนเกษียณใหม่");
        plan.setRetireAge(60);
        plan.setLifeExpectancy(90);
        plan.setInflationRate(3.0);
        plan.setPreRetireReturn(8.0);
        plan.setPostRetireReturn(3.0);
        return plan;
    }

    private RetirementBasicResult buildResult(RetirementBasic plan) {
        return RetirementBasicResult.builder()
                .planName(plan.getPlanName())
                .yearsToRetirement(plan.getRetireAge() - plan.getCurrentAge())
                .yearsInRetirement(plan.getLifeExpectancy() - plan.getRetireAge())
                .retirementMonthlyExpense(plan.getRetirementMonthlyExpense())
                .annualExpenseAtRetirement(plan.getAnnualExpenseAtRetirement())
                .totalFundsNeeded(plan.getTotalFundsNeeded())
                .requiredMonthlyInvestment(plan.getRequiredMonthlyInvestment())
                .build();
    }

    private String formatBaht(BigDecimal value) {
        if (value == null) {
            return "-";
        }
        DecimalFormat formatter = new DecimalFormat("#,##0.00");
        return "฿" + formatter.format(value);
    }
}
