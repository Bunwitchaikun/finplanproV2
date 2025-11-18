package com.finplanpro.finplanpro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/retirement/advanced")
public class RetirementAdvancedController {

    @GetMapping("/step1")
    public String showStep1() {
        return "retirement/advanced/step1";
    }

    @PostMapping("/step2")
    public String processStep1() {
        return "redirect:/retirement/advanced/step2";
    }

    @GetMapping("/step2")
    public String showStep2() {
        return "retirement/advanced/step2";
    }

    @PostMapping("/step3")
    public String processStep2() {
        return "redirect:/retirement/advanced/step3";
    }

    @GetMapping("/step3")
    public String showStep3() {
        return "retirement/advanced/step3";
    }

    @PostMapping("/step4")
    public String processStep3() {
        return "redirect:/retirement/advanced/step4";
    }

    @GetMapping("/step4")
    public String showStep4() {
        return "retirement/advanced/step4";
    }

    @PostMapping("/step5")
    public String processStep4() {
        return "redirect:/retirement/advanced/step5";
    }

    @GetMapping("/step5")
    public String showStep5() {
        return "retirement/advanced/step5";
    }

    @PostMapping("/step6")
    public String processStep5() {
        return "redirect:/retirement/advanced/step6";
    }

    @GetMapping("/step6")
    public String showStep6() {
        return "retirement/advanced/step6";
    }

    @PostMapping("/step7")
    public String processStep6() {
        return "redirect:/retirement/advanced/step7";
    }

    @GetMapping("/step7")
    public String showStep7() {
        return "retirement/advanced/step7";
    }
}
