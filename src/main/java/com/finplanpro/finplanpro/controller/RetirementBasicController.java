package com.finplanpro.finplanpro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/retirement/basic")
public class RetirementBasicController {

    @GetMapping
    public String showRetirementBasicForm() {
        return "retirement/retirement_basic_form";
    }
}
