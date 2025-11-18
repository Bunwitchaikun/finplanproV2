package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.RetirementAdvanced;
import com.finplanpro.finplanpro.service.RetirementAdvancedService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/retirement/advanced")
@SessionAttributes("plan")
public class RetirementAdvancedController {

    private final RetirementAdvancedService retirementAdvancedService;

    public RetirementAdvancedController(RetirementAdvancedService retirementAdvancedService) {
        this.retirementAdvancedService = retirementAdvancedService;
    }

    @ModelAttribute("plan")
    public RetirementAdvanced getPlan() {
        return new RetirementAdvanced();
    }

    private void addActiveMenu(Model model) {
        model.addAttribute("activeMenu", "retirement");
    }

    @GetMapping("/start")
    public String startNewPlan(SessionStatus status) {
        status.setComplete(); // Clear previous session attribute
        return "redirect:/retirement/advanced/step1";
    }

    @GetMapping("/step1")
    public String showStep1(Model model) {
        addActiveMenu(model);
        return "retirement/advanced/step1";
    }

    @PostMapping("/step1")
    public String processStep1(@ModelAttribute("plan") RetirementAdvanced plan) {
        return "redirect:/retirement/advanced/step2";
    }

    @GetMapping("/step2")
    public String showStep2(Model model) {
        addActiveMenu(model);
        return "retirement/advanced/step2";
    }

    @PostMapping("/step2")
    public String processStep2(@ModelAttribute("plan") RetirementAdvanced plan) {
        return "redirect:/retirement/advanced/step3";
    }

    @GetMapping("/step3")
    public String showStep3(Model model) {
        addActiveMenu(model);
        return "retirement/advanced/step3";
    }

    @PostMapping("/step3")
    public String processStep3(@ModelAttribute("plan") RetirementAdvanced plan) {
        return "redirect:/retirement/advanced/step4";
    }

    @GetMapping("/step4")
    public String showStep4(Model model) {
        addActiveMenu(model);
        return "retirement/advanced/step4";
    }

    @PostMapping("/step4")
    public String processStep4(@ModelAttribute("plan") RetirementAdvanced plan) {
        return "redirect:/retirement/advanced/step5";
    }

    @GetMapping("/step5")
    public String showStep5(Model model) {
        addActiveMenu(model);
        return "retirement/advanced/step5";
    }

    @PostMapping("/step5")
    public String processStep5(@ModelAttribute("plan") RetirementAdvanced plan) {
        return "redirect:/retirement/advanced/step6";
    }
    
    @GetMapping("/step6")
    public String showStep6(Model model, @ModelAttribute("plan") RetirementAdvanced plan) {
        addActiveMenu(model);
        retirementAdvancedService.calculate(plan); // Calculate results
        return "retirement/advanced/step6";
    }

    @PostMapping("/step6")
    public String processStep6(@ModelAttribute("plan") RetirementAdvanced plan) {
        return "redirect:/retirement/advanced/step7";
    }

    @GetMapping("/step7")
    public String showStep7(Model model) {
        addActiveMenu(model);
        return "retirement/advanced/step7";
    }

    @PostMapping("/save")
    public String savePlan(@ModelAttribute("plan") RetirementAdvanced plan, SessionStatus status, RedirectAttributes redirectAttributes) {
        retirementAdvancedService.save(plan);
        status.setComplete();
        redirectAttributes.addFlashAttribute("successMessage", "Advanced plan '" + plan.getPlanName() + "' saved successfully!");
        return "redirect:/retirement/basic"; // Redirect to the list view
    }
}
