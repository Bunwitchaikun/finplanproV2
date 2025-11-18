package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import com.finplanpro.finplanpro.service.RetirementBasicService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            model.addAttribute("newPlan", new RetirementBasic());
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

        RetirementBasic savedPlan = retirementBasicService.calculateAndSave(newPlan);
        redirectAttributes.addFlashAttribute("successMessage",
                "Plan '" + savedPlan.getPlanName() + "' saved successfully! Total funds needed: " + savedPlan.getTotalFundsNeeded());

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
}
