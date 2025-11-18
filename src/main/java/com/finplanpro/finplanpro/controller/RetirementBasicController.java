package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.service.RetirementBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/retirement/basic")
public class RetirementBasicController {

    @Autowired
    private RetirementBasicService retirementBasicService;

    @GetMapping
    public String showRetirementBasicList(@AuthenticationPrincipal User user, Model model) {
        List<RetirementBasic> retirementBasicList = retirementBasicService.findByUserId(user.getId());
        model.addAttribute("retirementBasicList", retirementBasicList);
        return "retirement/retirement_basic_list";
    }

    @GetMapping("/new")
    public String showRetirementBasicForm(Model model) {
        model.addAttribute("retirementBasic", new RetirementBasic());
        return "retirement/retirement_basic_form";
    }

    @PostMapping("/new")
    public String saveRetirementBasic(@AuthenticationPrincipal User user, RetirementBasic retirementBasic) {
        retirementBasic.setUser(user);
        retirementBasicService.save(retirementBasic);
        return "redirect:/retirement/basic";
    }

    @GetMapping("/{id}/delete")
    public String deleteRetirementBasic(@PathVariable Long id) {
        retirementBasicService.deleteById(id);
        return "redirect:/retirement/basic";
    }
}
