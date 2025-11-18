package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.RetirementAdvanced;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.service.RetirementAdvancedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/retirement/advanced")
@SessionAttributes("retirementAdvanced")
public class RetirementAdvancedController {

    @Autowired
    private RetirementAdvancedService retirementAdvancedService;

    @ModelAttribute("retirementAdvanced")
    public RetirementAdvanced getRetirementAdvanced() {
        return new RetirementAdvanced();
    }

    @GetMapping("/step1")
    public String showStep1(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "retirement/advanced/step1";
    }

    @PostMapping("/step2")
    public String processStep1(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "redirect:/retirement/advanced/step2";
    }

    @GetMapping("/step2")
    public String showStep2(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "retirement/advanced/step2";
    }

    @PostMapping("/step3")
    public String processStep2(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "redirect:/retirement/advanced/step3";
    }

    @GetMapping("/step3")
    public String showStep3(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "retirement/advanced/step3";
    }

    @PostMapping("/step4")
    public String processStep3(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "redirect:/retirement/advanced/step4";
    }

    @GetMapping("/step4")
    public String showStep4(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "retirement/advanced/step4";
    }

    @PostMapping("/step5")
    public String processStep4(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "redirect:/retirement/advanced/step5";
    }

    @GetMapping("/step5")
    public String showStep5(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "retirement/advanced/step5";
    }

    @PostMapping("/step6")
    public String processStep5(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "redirect:/retirement/advanced/step6";
    }



    @GetMapping("/step6")
    public String showStep6(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "retirement/advanced/step6";
    }

    @PostMapping("/step7")
    public String processStep6(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "redirect:/retirement/advanced/step7";
    }

    @GetMapping("/step7")
    public String showStep7(@ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced) {
        return "retirement/advanced/step7";
    }

    @PostMapping("/summary")
    public String processStep7(@AuthenticationPrincipal User user,
                               @ModelAttribute("retirementAdvanced") RetirementAdvanced retirementAdvanced,
                               SessionStatus sessionStatus,
                               RedirectAttributes redirectAttributes) {
        retirementAdvanced.setUser(user);
        retirementAdvancedService.save(retirementAdvanced);
        sessionStatus.setComplete();
        redirectAttributes.addFlashAttribute("success", "Retirement plan saved successfully!");
        return "redirect:/retirement/advanced/summary";
    }

    @GetMapping("/summary")
    public String showSummary(Model model) {
        return "retirement/advanced/summary";
    }
}
